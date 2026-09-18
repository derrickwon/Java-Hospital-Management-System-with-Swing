param([switch]$BuildOnly)
$ErrorActionPreference = 'Stop'
try {
    Set-Location -LiteralPath $PSScriptRoot
    $compiler = Get-Command javac -ErrorAction SilentlyContinue
    $runtime = Get-Command java -ErrorAction SilentlyContinue
    if (-not $compiler -or -not $runtime) {
        throw 'Java JDK is required. Install a JDK and ensure java and javac are on PATH.'
    }
    $build = Join-Path $PSScriptRoot '.run-build'
    New-Item -ItemType Directory -Path $build -Force | Out-Null
    [xml]$form = Get-Content -LiteralPath 'ManagementUI.form' -Raw
    $setup = [System.Collections.Generic.List[string]]::new()
    $setup.Add('mainPanel = new JPanel(new java.awt.GridBagLayout());')
    $setup.Add("mainPanel.setBackground(new java.awt.Color($($form.form.grid.properties.background.color)));")
    $setup.Add('mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20,25,20,25));')
    $index = 0
    foreach ($component in $form.form.grid.children.component) {
        $name = if ($component.binding) { [string]$component.binding } else { "formComponent$index" }
        $type = [string]$component.class
        $prefix = if ($component.binding) { '' } else { "$type " }
        $label = ([string]$component.properties.text.value).Replace('\', '\\').Replace('"', '\"').Replace("`r", '\r').Replace("`n", '\n')
        $setup.Add("$prefix$name = new $type(" + '"' + $label + '");')
        foreach ($property in @('background', 'foreground')) {
            $node = $component.properties.$property
            if ($node) {
                $method = if ($property -eq 'background') { 'setBackground' } else { 'setForeground' }
                $setup.Add("$name.$method(new java.awt.Color($($node.color)));")
            }
        }
        $font = $component.properties.font
        if ($font) {
            $size = if ($font.size) { $font.size } else { 16 }
            $style = if ($font.style) { $font.style } else { 0 }
            $setup.Add("$name.setFont(new java.awt.Font(" + '"' + $font.name + '", ' + $style + ', ' + $size + '));')
        }
        $grid = $component.constraints.grid
        $setup.Add("{ java.awt.GridBagConstraints c = new java.awt.GridBagConstraints(); c.gridx = $($grid.column); c.gridy = $($grid.row); c.gridwidth = $($grid.'col-span'); c.gridheight = $($grid.'row-span'); c.insets = new java.awt.Insets(5,5,5,5); c.fill = $($grid.fill); mainPanel.add($name,c); }")
        $index++
    }
    $source = Get-Content -LiteralPath 'ManagementUI.java' -Raw
    if (-not $source.Contains('public ManagementUI()')) { throw 'Cannot find ManagementUI constructor.' }
    $nl = [Environment]::NewLine
    $source = $source.Replace('public ManagementUI()', '{' + $nl + ($setup -join $nl) + $nl + '}' + $nl + 'public ManagementUI()')
    $generated = Join-Path $build 'ManagementUI.java'
    [System.IO.File]::WriteAllText($generated, $source, [System.Text.UTF8Encoding]::new($false))
    $sources = @(Get-ChildItem -LiteralPath $PSScriptRoot -Filter '*.java' | Where-Object Name -ne 'ManagementUI.java' | ForEach-Object FullName)
    & $compiler.Source -encoding UTF-8 -d $build @sources $generated
    if ($LASTEXITCODE -ne 0) { throw 'Java compilation failed. See the compiler errors above.' }
    if ($BuildOnly) { Write-Host 'Build successful.'; exit 0 }
    # Run in the foreground so startup errors stay visible in the launcher console.
    & $runtime.Source -cp $build Main
    if ($LASTEXITCODE -ne 0) { throw 'The application exited with an error.' }
} catch {
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

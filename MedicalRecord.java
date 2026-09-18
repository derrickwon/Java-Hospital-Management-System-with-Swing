public abstract class MedicalRecord {
    protected String diagnosis;
    protected String treatmentPlan;

    public MedicalRecord(String diagnosis, String treatmentPlan) {
        this.diagnosis = diagnosis;
        this.treatmentPlan = treatmentPlan;
    }


    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public String getDiagnosis() {
        return diagnosis;
    }
}


package com.example.v3.member.plan_adapter.dto;

import java.io.Serializable;

public class AddPlanDto implements Serializable {

    private String saveWeight;
    private String saveExercise;
    private String saveReps;

    public AddPlanDto(String saveWeight, String saveExercise, String saveReps) {
        this.saveWeight = saveWeight;
        this.saveExercise = saveExercise;
        this.saveReps = saveReps;
    }

    public void saveAll(String saveWeight, String saveExercise, String saveReps){
        this.saveWeight = saveWeight;
        this.saveExercise = saveExercise;
        this.saveReps = saveReps;
    }

    public String getSaveWeight() {
        return saveWeight;
    }

    public String getSaveExercise() {
        return saveExercise;
    }

    public String getSaveReps() {
        return saveReps;
    }

}

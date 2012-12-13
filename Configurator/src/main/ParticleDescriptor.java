package main;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author roudnev
 */
public class ParticleDescriptor {
   private double m;
   private boolean ifBoson;
   private String description;
   public  ParticleDescriptor(){
       m=1.0;
       ifBoson = true;
       description = "Unknown";
   }
    /**
     * @return the m
     */
    public double getM() {
        return m;
    }

    /**
     * @param m the m to set
     */
    public void setM(double m) {
        this.m = m;
    }

    /**
     * @return the ifBoson
     */
    public boolean isIfBoson() {
        return ifBoson;
    }

    /**
     * @param ifBoson the ifBoson to set
     */
    public void setIfBoson(boolean ifBoson) {
        this.ifBoson = ifBoson;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

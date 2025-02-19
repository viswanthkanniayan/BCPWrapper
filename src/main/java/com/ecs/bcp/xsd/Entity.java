package com.ecs.bcp.xsd;

public class Entity {

	 private String name;       // Field for entity name
	    private String firmRegNo;
		public Entity(String name, String firmRegNo) {
			this.name = name;
			this.firmRegNo = firmRegNo;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getFirmRegNo() {
			return firmRegNo;
		}
		public void setFirmRegNo(String firmRegNo) {
			this.firmRegNo = firmRegNo;
		} 
	    
		@Override
	    public String toString() {
	        return "{name = \"" + name + "\", firmRegNo = \"" + firmRegNo + "\"}";
	    }
}

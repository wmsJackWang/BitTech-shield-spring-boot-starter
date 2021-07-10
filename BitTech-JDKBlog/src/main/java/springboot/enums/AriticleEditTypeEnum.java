package springboot.enums;

import springboot.enums.base.BaseEnum;

public enum AriticleEditTypeEnum  implements BaseEnum  {

	LOCKED("LOCKED");

	private String editType;
	
	private AriticleEditTypeEnum(String type) {
		editType = type;
	}
	
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return editType;
	}
	
	

}

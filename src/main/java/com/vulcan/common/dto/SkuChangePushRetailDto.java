package com.vulcan.common.dto;

/**
 * @Author: likaisheng
 * @Description:
 * @Date: Created in 15:46 2017/12/3
 * @Modified By:
 */

public class SkuChangePushRetailDto {
    private Long skuId;
    private Long wareId;

    
    public SkuChangePushRetailDto() {
		super();
	}

	public SkuChangePushRetailDto(Long skuId, Long wareId) {
        this.skuId = skuId;
        this.wareId = wareId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getWareId() {
        return wareId;
    }

    public void setWareId(Long wareId) {
        this.wareId = wareId;
    }
}

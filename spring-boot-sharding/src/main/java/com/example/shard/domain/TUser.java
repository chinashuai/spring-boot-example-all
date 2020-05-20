package com.example.shard.domain;



/**
 * 
 * t_user分库分表
 * 
 **/
public class TUser implements java.io.Serializable {


  private static final long serialVersionUID = 1L;


  /****/

  private Long id;


  /****/

  private Long userId;


  /****/

  private String remark;




  public void setId(Long id) { 
    this.id = id;
  }


  public Long getId() { 
    return this.id;
  }


  public void setUserId(Long userId) { 
    this.userId = userId;
  }


  public Long getUserId() { 
    return this.userId;
  }


  public void setRemark(String remark) { 
    this.remark = remark;
  }


  public String getRemark() { 
    return this.remark;
  }

}

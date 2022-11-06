package com.MySchool.master.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_error_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblErrorMessages implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  long id;

	@NotEmpty
	@Column(columnDefinition = " VARCHAR(255) ")
	private String errorCode;
	
	@NotEmpty
	@Column(columnDefinition=" TEXT NOT NULL")
	private String errorMessage;

	
	@Column(name = "created_at", columnDefinition=" TIMESTAMP")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", columnDefinition=" TIMESTAMP ")
	private LocalDateTime updatedAt;

}

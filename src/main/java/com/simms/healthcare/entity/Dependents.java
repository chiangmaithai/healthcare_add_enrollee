package com.simms.healthcare.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The model package should be put into its own jar and added as a dependency to the microservices.
 * Entity for dependents.
 * @author thoma
 *
 */
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Dependents implements Serializable{

		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		private Long id;
		
		@NonNull 
		private String dependentId;
		
		@NonNull
		private String name;

		/**
		 * Format yyyyMMdd
		 */
		@NonNull
		@Column(length=8, nullable=false)
		private Integer birthdDate;
		
		@NonNull
		@Column(unique=true, nullable=false, length=9)
		private String ssn;
		
		@Version
		@Column(name="UPDATE_TS")
		private LocalDateTime lastDateModified;
		
		@Column(name = "CREATION_TS", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
			insertable=false, updatable=false)
		private LocalDateTime createdDate;
}

package com.simms.healthcare.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.simms.healthcare.service.ActivationStatusCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Entity for Enrollee.
 * @author thoma
 *
 */
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Enrollee implements Serializable{

		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		private Long id;
		
		@NonNull 
		private String enrolleeId;
		
		@NonNull
		private String name;
		
		@NonNull
		private ActivationStatusCode activationStatus;
		
		/**
		 * Format yyyyMMdd
		 */
		@NonNull
		private Integer birthdDate;

		/**
		 * Any format
		 */
		private String phoneNumber;
		
		@NonNull
		private String ssn;
		
		@NonNull
		@Version
		@Column(name="UPDATE_TS")
		private LocalDateTime lastDateModified;
		
		@NonNull
		@Column(name = "CREATION_TS", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
			insertable=false, updatable=false)
		private LocalDateTime createdDate;
		
		@OneToMany(targetEntity=Dependents.class, cascade = {CascadeType.ALL})
		@Fetch(FetchMode.JOIN)
		private List<Dependents> dependentList;
}

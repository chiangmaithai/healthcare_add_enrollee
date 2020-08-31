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

import com.simms.healthcare.businesslogic.ActivationStatusCode;

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

		/**
	 * 
	 */
	private static final long serialVersionUID = 5663316888621088761L;

		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		private Long id;
		
		@NonNull
		@Column(unique=true, nullable=false)
		private String enrolleeId;
		
		@NonNull
		private String name;
		
		@NonNull
		private ActivationStatusCode activationStatus;
		
		/**
		 * Format yyyyMMdd
		 */
		@NonNull
		@Column(length=8, nullable=false)
		private Integer birthDate;

		/**
		 * Any format
		 */
		@Column(nullable=true, length=25)
		private String phoneNumber;
		
		@NonNull
		@Column(unique=true, nullable=false, length=9)
		private String ssn;
		
		@Version
		@Column(name="UPDATE_TS")
		private LocalDateTime lastDateModified;
		
		@Column(name = "CREATION_TS", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
			insertable=false, updatable=false)
		private LocalDateTime createdDate;
		
		@OneToMany(targetEntity=Dependents.class, cascade = {CascadeType.ALL})
		@Fetch(FetchMode.JOIN)
		private List<Dependents> dependentList;
}

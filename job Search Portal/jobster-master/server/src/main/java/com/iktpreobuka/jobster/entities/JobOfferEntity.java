package com.iktpreobuka.jobster.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "job_offers")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class JobOfferEntity {
	
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;


	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "employer")
	@NotNull (message = "Employer must be provided.")
	private UserEntity employer;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "city")
	@NotNull (message = "City must be provided.")
	private CityEntity city;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "type")
	@NotNull (message = "Job type must be provided.")
	private JobTypeEntity type;
	
	@JsonIgnore
	@OneToMany(mappedBy = "offer", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<JobDayHoursEntity> daysAndHours = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "offer", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<ApplyContactEntity> applies = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "rejectedOffer", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<RejectOfferEntity> rejections = new ArrayList<>();

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@JsonView(Views.Parent.class)
	@Column(name="offer_id")
	protected Integer id;
	
	//@JsonView(Views.Student.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@NotNull (message = "Beginning date must be provided.")
	@Column(name="beginning_date")
	private Date beginningDate;
	
	//@JsonView(Views.Student.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@NotNull (message = "End date must be provided.")
	@Column(name="end_date")
	private Date endDate;
	
	//@JsonView(Views.Student.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name="date_Created")
	private Date dateCreated;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name="date_Updated")
	private Date dateUpdated;
	
	@Column(name="flexibile_dates")
	//@JsonView(Views.Admin.class)
	private Boolean flexibileDates;
	
	//@JsonView(Views.Student.class)
	@Column(name="number_of_employees")
	@NotNull (message = "Number of employees must be provided.")
	@Min(value=1, message = "Number of employees must be {value} or higher!")
	private Integer numberOfEmployees;
	
	//@JsonView(Views.Student.class)
	@Column(name="price")
	@NotNull (message = "Price must be provided.")
	@Min(value=0, message = "Price must be {value} or higher!")
	private Double price;
	
	//@JsonView(Views.Teacher.class)
	@Column(name="details_link")
	@NotNull (message = "Details must be provided.")
	private String detailsLink;
	
	@Column(name="flexibile_days")
	//@JsonView(Views.Admin.class)
	private Boolean flexibileDays;
	
	@Column(name="counter_offer")
	//@JsonView(Views.Admin.class)
	@NotNull (message = "Counter offer must be provided.")
	private Boolean counterOffer;
	
	//@JsonView(Views.Admin.class)
	@Max(1)
    @Min(-1)
    @Column(name = "status", nullable = false)
	private Integer status;
	
	//@JsonView(Views.Admin.class)
    @Column(name = "expired", nullable = false)
	private Boolean expired;
	
	//@JsonView(Views.Admin.class)
    @Column(name = "created_by", nullable = false, updatable = false)
	private Integer createdById;
    
    //@JsonView(Views.Admin.class)
    @Column(name = "updated_by")
    private Integer updatedById;
    
	@JsonIgnore
	@Version
	private Integer version;

	
	public JobOfferEntity() {
		super();
	}

	public JobOfferEntity(@NotNull(message = "Employer must be provided.") UserEntity employer,
			@NotNull(message = "City must be provided.") CityEntity city,
			@NotNull(message = "Job type must be provided.") JobTypeEntity type, List<JobDayHoursEntity> daysAndHours,
			@NotNull(message = "Beginning date must be provided.") Date beginningDate,
			@NotNull(message = "End date must be provided.") Date endDate, Boolean flexibileDates,
			@NotNull(message = "Number of employees must be provided.") @Min(value = 1, message = "Number of employees must be {value} or higher!") Integer numberOfEmployees,
			@NotNull(message = "Price must be provided.") @Min(value = 0, message = "Price must be {value} or higher!") Double price,
			@NotNull(message = "Details must be provided.") String detailsLink, Boolean flexibileDays,
			Integer createdById) {
		super();
		this.employer = employer;
		this.city = city;
		this.type = type;
		this.daysAndHours = daysAndHours;
		this.beginningDate = beginningDate;
		this.endDate = endDate;
		this.flexibileDates = flexibileDates;
		this.numberOfEmployees = numberOfEmployees;
		this.price = price;
		this.detailsLink = detailsLink;
		this.flexibileDays = flexibileDays;
		this.counterOffer = false;
		this.status = getStatusActive();
		this.expired = false;
		this.createdById = createdById;
	}

	public JobOfferEntity(@NotNull(message = "Employer must be provided.") UserEntity employer,
			@NotNull(message = "City must be provided.") CityEntity city,
			@NotNull(message = "Job type must be provided.") JobTypeEntity type, List<JobDayHoursEntity> daysAndHours,
			@NotNull(message = "Beginning date must be provided.") Date beginningDate,
			@NotNull(message = "End date must be provided.") Date endDate, Boolean flexibileDates,
			@NotNull(message = "Number of employees must be provided.") @Min(value = 1, message = "Number of employees must be {value} or higher!") Integer numberOfEmployees,
			@NotNull(message = "Price must be provided.") @Min(value = 0, message = "Price must be {value} or higher!") Double price,
			@NotNull(message = "Details must be provided.") String detailsLink, Boolean flexibileDays,
			@NotNull (message = "Counter offer must be provided.") Boolean counterOffer,
			Integer createdById) {
		super();
		this.employer = employer;
		this.city = city;
		this.type = type;
		this.daysAndHours = daysAndHours;
		this.beginningDate = beginningDate;
		this.endDate = endDate;
		this.flexibileDates = flexibileDates;
		this.numberOfEmployees = numberOfEmployees;
		this.price = price;
		this.detailsLink = detailsLink;
		this.flexibileDays = flexibileDays;
		this.counterOffer = counterOffer;
		this.status = getStatusActive();
		this.expired = false;
		this.createdById = createdById;
	}
	
	public JobOfferEntity(@NotNull(message = "Employer must be provided.") UserEntity employer,
			@NotNull(message = "City must be provided.") CityEntity city,
			@NotNull(message = "Job type must be provided.") JobTypeEntity type, List<JobDayHoursEntity> daysAndHours,
			List<ApplyContactEntity> applies, List<RejectOfferEntity> rejections, Integer id,
			@NotNull(message = "Beginning date must be provided.") Date beginningDate,
			@NotNull(message = "End date must be provided.") Date endDate, Date dateCreated, Boolean flexibileDates,
			@NotNull(message = "Number of employees must be provided.") @Min(value = 1, message = "Number of employees must be {value} or higher!") Integer numberOfEmployees,
			@NotNull(message = "Price must be provided.") @Min(value = 0, message = "Price must be {value} or higher!") Double price,
			@NotNull(message = "Details must be provided.") String detailsLink, Boolean flexibileDays,
			@NotNull(message = "Counter offer must be provided.") Boolean counterOffer, @Max(1) @Min(-1) Integer status,
			Boolean expired, Integer createdById, Integer updatedById, Integer version) {
		super();
		this.employer = employer;
		this.city = city;
		this.type = type;
		this.daysAndHours = daysAndHours;
		this.applies = applies;
		this.rejections = rejections;
		this.id = id;
		this.beginningDate = beginningDate;
		this.endDate = endDate;
		this.dateCreated = Calendar.getInstance().getTime();
		this.flexibileDates = flexibileDates;
		this.numberOfEmployees = numberOfEmployees;
		this.price = price;
		this.detailsLink = detailsLink;
		this.flexibileDays = flexibileDays;
		this.counterOffer = false;
		this.status = getStatusActive();
		this.expired = false;
		this.createdById = createdById;
		this.updatedById = updatedById;
		this.version = version;
	}
	
	
	
	public JobOfferEntity(@NotNull(message = "Employer must be provided.") UserEntity employer,
			@NotNull(message = "City must be provided.") CityEntity city,
			@NotNull(message = "Job type must be provided.") JobTypeEntity type, List<JobDayHoursEntity> daysAndHours,
			List<ApplyContactEntity> applies, List<RejectOfferEntity> rejections, Integer id,
			@NotNull(message = "Beginning date must be provided.") Date beginningDate,
			@NotNull(message = "End date must be provided.") Date endDate, Date dateCreated, Date dateUpdated,
			Boolean flexibileDates,
			@NotNull(message = "Number of employees must be provided.") @Min(value = 1, message = "Number of employees must be {value} or higher!") Integer numberOfEmployees,
			@NotNull(message = "Price must be provided.") @Min(value = 0, message = "Price must be {value} or higher!") Double price,
			@NotNull(message = "Details must be provided.") String detailsLink, Boolean flexibileDays,
			@NotNull(message = "Counter offer must be provided.") Boolean counterOffer, @Max(1) @Min(-1) Integer status,
			Boolean expired, Integer createdById, Integer updatedById, Integer version) {
		super();
		this.employer = employer;
		this.city = city;
		this.type = type;
		this.daysAndHours = daysAndHours;
		this.applies = applies;
		this.rejections = rejections;
		this.id = id;
		this.beginningDate = beginningDate;
		this.endDate = endDate;
		this.dateCreated = dateCreated;
		this.dateUpdated = dateUpdated;
		this.flexibileDates = flexibileDates;
		this.numberOfEmployees = numberOfEmployees;
		this.price = price;
		this.detailsLink = detailsLink;
		this.flexibileDays = flexibileDays;
		this.counterOffer = false;
		this.status = getStatusActive();
		this.expired = false;
		this.createdById = createdById;
		this.updatedById = updatedById;
		this.version = version;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getCounterOffer() {
		return counterOffer;
	}

	public void setCounterOffer(Boolean counterOffer) {
		this.counterOffer = counterOffer;
	}

	public List<ApplyContactEntity> getApplies() {
		return applies;
	}

	public void setApplies(List<ApplyContactEntity> applies) {
		this.applies = applies;
	}

	public UserEntity getEmployer() {
		return employer;
	}

	public void setEmployer(UserEntity employer) {
		this.employer = employer;
	}

	public CityEntity getCity() {
		return city;
	}

	public void setCity(CityEntity city) {
		this.city = city;
	}

	public JobTypeEntity getType() {
		return type;
	}

	public void setType(JobTypeEntity type) {
		this.type = type;
	}

	public List<JobDayHoursEntity> getDaysAndHours() {
		return daysAndHours;
	}

	public void setDaysAndHours(List<JobDayHoursEntity> daysAndHours) {
		this.daysAndHours = daysAndHours;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getBeginningDate() {
		return beginningDate;
	}

	public void setBeginningDate(Date beginningDate) {
		this.beginningDate = beginningDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getFlexibileDates() {
		return flexibileDates;
	}

	public void setFlexibileDates(Boolean flexibileDates) {
		this.flexibileDates = flexibileDates;
	}

	public Integer getNumberOfEmployees() {
		return numberOfEmployees;
	}

	public void setNumberOfEmployees(Integer numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDetailsLink() {
		return detailsLink;
	}

	public void setDetailsLink(String detailsLink) {
		this.detailsLink = detailsLink;
	}

	public Boolean getFlexibileDays() {
		return flexibileDays;
	}

	public void setFlexibileDays(Boolean flexibileDays) {
		this.flexibileDays = flexibileDays;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public Integer getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Integer updatedById) {
		this.updatedById = updatedById;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatusInactive() {
		this.status = getStatusInactive();
	}

	public void setStatusActive() {
		this.status = getStatusActive();
	}

	public void setStatusArchived() {
		this.status = getStatusArchived();
	}
	
	public static Integer getStatusInactive() {
		return STATUS_INACTIVE;
	}

	public static Integer getStatusActive() {
		return STATUS_ACTIVE;
	}

	public static Integer getStatusArchived() {
		return STATUS_ARCHIVED;
	}

	public List<RejectOfferEntity> getRejections() {
		return rejections;
	}

	public void setRejections(List<RejectOfferEntity> rejections) {
		this.rejections = rejections;
	}

}

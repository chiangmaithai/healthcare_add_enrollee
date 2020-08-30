# healthcare_add_enrollee
Microservice to add new enrollees to healthcare

Business Requirements:

1. Add new enrollee - 
	test case - 
		EnrolleeRepositoryTest.whenSavingEnrolleeAndDependents_thenCorrect
		EnrolleeRepositoryTest.whenSavingEnrollee_thenCorrect


2. Modify an existing enrollee -
	test case
		EnrolleeServiceTest.whenModifyingEnrollee_thenCorrect


3. Remove an enrollee entirely
	test case
		EnrolleeRepositoryTest.whenSavingEnrolleeThenDeleleEnrollee_thenCorrect
		EnrolleeRepositoryTest.whenSavingEnrolleeThenDeleleEnrolleeAndDependents_thenCorrect

4. Add dependents
	test case - 
		EnrolleeRepositoryTest.whenSavingEnrolleeAndDependents_thenCorrect

5. Remove dependents from enrollee - 
	test case - 
		EnrolleeRepositoryTest.whenRemovingOneOfManyDependents_thenCorrect


6. Modify existing dependents 





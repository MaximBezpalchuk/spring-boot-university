package com.foxminded.university;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;

public class MenuCreator {

	private boolean exit;
	private BufferedReader reader;
	private Cathedra cathedra;
	Formatter formatter = new Formatter();
	DataUpdater dataUpdater = new DataUpdater();
	DataCreator dataCreator = new DataCreator();

	public MenuCreator(Cathedra cathedra) {
		this.cathedra = cathedra;
	}

	private String printMainMenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("=================");
		menu.append(System.lineSeparator());
		menu.append("Chose what you want to do:");
		menu.append(System.lineSeparator());
		menu.append("1  - Create");
		menu.append(System.lineSeparator());
		menu.append("2  - Get info");
		menu.append(System.lineSeparator());
		menu.append("3  - Update");
		menu.append(System.lineSeparator());
		menu.append("4  - Delete");
		menu.append(System.lineSeparator());
		menu.append("0  - Exit");

		return menu.toString();
	}

	private String printCreateMenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("Chose what you want to create:");
		menu.append(System.lineSeparator());
		menu.append("1  - Create new student");
		menu.append(System.lineSeparator());
		menu.append("2  - Create new teacher");
		menu.append(System.lineSeparator());
		menu.append("3  - Create new subject");
		menu.append(System.lineSeparator());
		menu.append("4  - Create new student group");
		menu.append(System.lineSeparator());
		menu.append("5  - Create new lecture");
		menu.append(System.lineSeparator());
		menu.append("6  - Create new holiday");
		menu.append(System.lineSeparator());
		menu.append("7  - Create new audience");
		menu.append(System.lineSeparator());
		menu.append("0  - Go to the main menu");

		return menu.toString();
	}

	private String printReadMenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("Chose what you want to get:");
		menu.append(System.lineSeparator());
		menu.append("1  - Get student list");
		menu.append(System.lineSeparator());
		menu.append("2  - Get teacher list");
		menu.append(System.lineSeparator());
		menu.append("3  - Get subject list");
		menu.append(System.lineSeparator());
		menu.append("4  - Get group list");
		menu.append(System.lineSeparator());
		menu.append("5  - Get holiday list");
		menu.append(System.lineSeparator());
		menu.append("6  - Get timetable for student - day");
		menu.append(System.lineSeparator());
		menu.append("7  - Get timetable for teacher - day");
		menu.append(System.lineSeparator());
		menu.append("8  - Get timetable for student - month");
		menu.append(System.lineSeparator());
		menu.append("9  - Get timetable for teacher - month");
		menu.append(System.lineSeparator());
		menu.append("10 - Get audience list");
		menu.append(System.lineSeparator());
		menu.append("0  - Go to the main menu");

		return menu.toString();
	}

	private String printUpdateMenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("Chose what you want to update:");
		menu.append(System.lineSeparator());
		menu.append("1  - Set student to group");
		menu.append(System.lineSeparator());
		menu.append("2  - Set teacher vacation");
		menu.append(System.lineSeparator());
		menu.append("3  - Set subject to teacher");
		menu.append(System.lineSeparator());
		menu.append("4  - Change lecture audience");
		menu.append(System.lineSeparator());
		menu.append("5  - Change lecture date");
		menu.append(System.lineSeparator());
		menu.append("6  - Change lecture time");
		menu.append(System.lineSeparator());
		menu.append("7  - Set groups to lectures");
		menu.append(System.lineSeparator());
		menu.append("0  - Go to the main menu");

		return menu.toString();
	}

	private String printDeleteMenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("Chose what you want to delete:");
		menu.append(System.lineSeparator());
		menu.append("1  - Delete student");
		menu.append(System.lineSeparator());
		menu.append("2  - Delete teacher");
		menu.append(System.lineSeparator());
		menu.append("3  - Delete group");
		menu.append(System.lineSeparator());
		menu.append("4  - Delete lecture");
		menu.append(System.lineSeparator());
		menu.append("5  - Delete audience");
		menu.append(System.lineSeparator());
		menu.append("6  - Delete subject");
		menu.append(System.lineSeparator());
		menu.append("7  - Delete teacher vacation");
		menu.append(System.lineSeparator());
		menu.append("8  - Delete holiday");
		menu.append(System.lineSeparator());
		menu.append("0  - Go to the main menu");

		return menu.toString();
	}

	private int getInput(int max) {
		int choise = -1;
		while (choise < 0 || choise > max) {
			try {
				choise = Integer.parseInt(reader.readLine());
			} catch (NumberFormatException e) {
				System.out.println("Invalid selection. Please try again");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return choise;
	}

	public void buildMenu() {
		reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (!exit) {
				System.out.println(printMainMenu());
				int choise = getInput(4);
				switch (choise) {
				case 0:
					exit = true;
					System.out.println("Exit completed!");
					break;
				case 1:
					submenuCreate();
					break;
				case 2:
					submenuRead();
					break;
				case 3:
					submenuUpdate();
					break;
				case 4:
					submenuDelete();
					break;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void submenuCreate() throws IOException {
		System.out.println(printCreateMenu());
		int choise = getInput(7);
		switch (choise) {
		case 1:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Enter student first name:");
			String firstName1 = reader.readLine();
			exitCheck(firstName1);
			System.out.println("Enter student last name:");
			String lastName1 = reader.readLine();
			exitCheck(lastName1);
			System.out.println("Enter student phone:");
			String phone1 = reader.readLine();
			exitCheck(phone1);
			System.out.println("Enter student address:");
			String address1 = reader.readLine();
			exitCheck(address1);
			System.out.println("Enter student e-mail:");
			String email1 = reader.readLine();
			exitCheck(email1);
			System.out.println("Enter student gender:");
			String gender1 = reader.readLine();
			exitCheck(gender1);
			System.out.println("Enter student postal code:");
			String postalCode1 = reader.readLine();
			exitCheck(postalCode1);
			System.out.println("Enter student education level:");
			String education1 = reader.readLine();
			exitCheck(education1);
			System.out.println("Enter the date of birth separated by commas without spaces (YEAR,MONTH,DAY):");
			String birthDateString1 = reader.readLine();
			exitCheck(birthDateString1);
			String[] birthArr1 = birthDateString1.split(",");
			int birthYear1 = Integer.parseInt(birthArr1[0]);
			int birthMonth1 = Integer.parseInt(birthArr1[1]);
			int birthDay1 = Integer.parseInt(birthArr1[2]);
			LocalDate birthDate1 = LocalDate.of(birthYear1, birthMonth1, birthDay1);
			System.out.println("Set group from list:");
			List<Group> sortedGroups1 = sortGroupsByName(cathedra.getGroups());
			System.out.println(formatter.formatGroupList(sortedGroups1));
			String groupNumber1 = reader.readLine();
			exitCheck(groupNumber1);
			Group group1 = sortedGroups1.get(Integer.parseInt(groupNumber1) - 1);
			dataUpdater.createStudent(firstName1, lastName1, phone1, address1, email1, gender1, postalCode1, education1,
					birthDate1, group1);
			System.out.println("Student added!");
			break;
		case 2:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Enter teacher first name:");
			String firstName2 = reader.readLine();
			exitCheck(firstName2);
			System.out.println("Enter teacher last name:");
			String lastName2 = reader.readLine();
			exitCheck(lastName2);
			System.out.println("Enter teacher phone:");
			String phone2 = reader.readLine();
			exitCheck(phone2);
			System.out.println("Enter teacher address:");
			String address2 = reader.readLine();
			exitCheck(address2);
			System.out.println("Enter teacher e-mail:");
			String email2 = reader.readLine();
			exitCheck(email2);
			System.out.println("Enter teacher gender:");
			String gender2 = reader.readLine();
			exitCheck(gender2);
			System.out.println("Enter teacher postal code:");
			String postalCode2 = reader.readLine();
			exitCheck(postalCode2);
			System.out.println("Enter teacher education level:");
			String education2 = reader.readLine();
			exitCheck(education2);
			System.out.println("Enter the date of birth separated by commas without spaces (YEAR,MONTH,DAY):");
			String birthDateString2 = reader.readLine();
			exitCheck(birthDateString2);
			String[] birthArr2 = birthDateString2.split(",");
			int birthYear2 = Integer.parseInt(birthArr2[0]);
			int birthMonth2 = Integer.parseInt(birthArr2[1]);
			int birthDay2 = Integer.parseInt(birthArr2[2]);
			LocalDate birthDate2 = LocalDate.of(birthYear2, birthMonth2, birthDay2);
			System.out.println("Set subject from list:");
			List<Subject> sortedSubjects2 = sortSubjectsByName(cathedra.getSubjects());
			System.out.println(formatter.formatSubjectList(sortedSubjects2));
			String subjectNumber2 = reader.readLine();
			exitCheck(subjectNumber2);
			String[] subjectsArr2 = subjectNumber2.split(",");
			List<Subject> subjects2 = new ArrayList<>();
			for (String subjNum : subjectsArr2) {
				subjects2.add(sortedSubjects2.get(Integer.parseInt(subjNum) - 1));
			}
			dataUpdater.createTeacher(firstName2, lastName2, phone2, address2, email2, gender2, postalCode2, education2,
					birthDate2, cathedra, subjects2);
			System.out.println("Teacher added!");
			break;
		case 3:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Enter subject name:");
			String subjectName3 = reader.readLine();
			exitCheck(subjectName3);
			System.out.println("Enter subject description:");
			String subjectDescription3 = reader.readLine();
			exitCheck(subjectDescription3);
			Subject subject3 = new Subject(subjectName3, subjectDescription3);
			cathedra.getSubjects().add(subject3);
			System.out.println("Subject added!");
			break;
		case 4:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Enter group name:");
			String groupName4 = reader.readLine();
			exitCheck(groupName4);
			Group group4 = new Group(groupName4, cathedra);
			cathedra.getGroups().add(group4);
			System.out.println("Group added!");
			break;
		case 5:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Set subject from the list:");
			List<Subject> sortedSubjects5 = sortSubjectsByName(cathedra.getSubjects());
			System.out.println(formatter.formatSubjectList(sortedSubjects5));
			String subjectNumber5 = reader.readLine();
			exitCheck(subjectNumber5);
			Subject subject5 = sortedSubjects5.get(Integer.parseInt(subjectNumber5) - 1);
			System.out.println("Set teacher from the list:");
			List<Teacher> sortedTeachers5 = sortTeachersByLastName(cathedra.getTeachers());
			System.out.println(formatter.formatTeacherList(sortedTeachers5));
			String teacherNumber5 = reader.readLine();
			exitCheck(teacherNumber5);
			Teacher teacher5 = sortedTeachers5.get(Integer.parseInt(teacherNumber5) - 1);
			System.out.println("Set audience from the list:");
			List<Audience> sortedAudiences = sortAudiencesByNumber(cathedra.getAudiences());
			System.out.println(formatter.formatAudienceList(sortedAudiences));
			String audienceNumber5 = reader.readLine();
			exitCheck(audienceNumber5);
			Audience audience5 = sortedAudiences.get(Integer.parseInt(audienceNumber5) - 1);
			System.out.println("Enter the lecture date separated by commas without spaces (YEAR,MONTH,DAY):");
			String lectureDateString5 = reader.readLine();
			exitCheck(lectureDateString5);
			String[] lectureArr5 = lectureDateString5.split(",");
			int lectureYear5 = Integer.parseInt(lectureArr5[0]);
			int lectureMonth5 = Integer.parseInt(lectureArr5[1]);
			int lectureDay5 = Integer.parseInt(lectureArr5[2]);
			LocalDate lectureDate5 = LocalDate.of(lectureYear5, lectureMonth5, lectureDay5);
			// TODO: remake to dependency on entity
			System.out.println("Set lecture time from the list:");
			System.out.println("1. " + LocalTime.of(8, 0) + " - " + LocalTime.of(9, 30));
			System.out.println("2. " + LocalTime.of(9, 40) + " - " + LocalTime.of(11, 10));
			System.out.println("3. " + LocalTime.of(11, 20) + " - " + LocalTime.of(12, 50));
			System.out.println("4. " + LocalTime.of(13, 20) + " - " + LocalTime.of(14, 50));
			System.out.println("5. " + LocalTime.of(15, 0) + " - " + LocalTime.of(16, 30));
			System.out.println("6. " + LocalTime.of(16, 40) + " - " + LocalTime.of(18, 10));
			System.out.println("7. " + LocalTime.of(18, 20) + " - " + LocalTime.of(19, 50));
			System.out.println("8. " + LocalTime.of(20, 0) + " - " + LocalTime.of(21, 30));
			int lectureTimeInt5 = Integer.parseInt(reader.readLine());
			// TODO: remake to entity
			LectureTime lectureTime5 = dataCreator.createLectureTime(lectureTimeInt5);
			Lecture lecture5 = new Lecture(subject5, lectureDate5, lectureTime5, audience5, teacher5);
			cathedra.getLectures().add(lecture5);
			System.out.println("Lecture added!");
			break;
		case 6:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Enter holiday description");
			String holidayDescription6 = reader.readLine();
			exitCheck(holidayDescription6);
			System.out.println("Enter the holiday date separated by commas without spaces (YEAR,MONTH,DAY):");
			String holidayDateString5 = reader.readLine();
			exitCheck(holidayDateString5);
			String[] holidayArr5 = holidayDateString5.split(",");
			int holidayYear5 = Integer.parseInt(holidayArr5[0]);
			int holidayMonth5 = Integer.parseInt(holidayArr5[1]);
			int holidayDay5 = Integer.parseInt(holidayArr5[2]);
			LocalDate holidayDate5 = LocalDate.of(holidayYear5, holidayMonth5, holidayDay5);
			Holiday holiday6 = new Holiday(holidayDescription6, holidayDate5);
			cathedra.getHolidays().add(holiday6);
			System.out.println("Holiday created!");
			break;
		case 7:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Enter audience room");
			String audienceRoom7 = reader.readLine();
			exitCheck(audienceRoom7);
			System.out.println("Enter audience capacity");
			String audienceCapacity7 = reader.readLine();
			exitCheck(audienceCapacity7);
			Audience audience7 = new Audience(Integer.parseInt(audienceRoom7), Integer.parseInt(audienceCapacity7));
			cathedra.getAudiences().add(audience7);
			System.out.println("Audience created!");
			break;
		case 0:
			break;
		}
	}

	private void submenuRead() throws IOException {
		System.out.println(printReadMenu());
		int choise = getInput(10);
		switch (choise) {
		case 1:
			List<Student> students1 = new ArrayList<>();
			cathedra.getGroups().stream().forEach(group -> group.getStudents().stream()
					.filter(student -> !students1.contains(student)).forEach(student -> students1.add(student)));
			System.out.println(formatter.formatStudentList(sortStudentsByLastName(students1)));
			break;
		case 2:
			System.out.println(formatter.formatTeacherList(sortTeachersByLastName(cathedra.getTeachers())));
			break;
		case 3:
			System.out.println(formatter.formatSubjectList(sortSubjectsByName(cathedra.getSubjects())));
			break;
		case 4:
			System.out.println(formatter.formatGroupList(sortGroupsByName(cathedra.getGroups())));
			break;
		case 5:
			System.out.println(formatter.formatHolidayList(sortHolidaysByDate(cathedra.getHolidays())));
			break;
		case 6:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select student from the list:");
			List<Student> students6 = new ArrayList<>();
			cathedra.getGroups().stream().forEach(group -> group.getStudents().stream()
					.filter(student -> !students6.contains(student)).forEach(student -> students6.add(student)));
			List<Student> sortedStudents6 = sortStudentsByLastName(students6);
			System.out.println(formatter.formatStudentList(sortedStudents6));
			String studentNumber6 = reader.readLine();
			exitCheck(studentNumber6);
			Student student6 = sortedStudents6.get(Integer.parseInt(studentNumber6) - 1);
			System.out.println("Enter date separated by commas without spaces (MONTH,DAY):");
			String lactureDateString6 = reader.readLine();
			exitCheck(lactureDateString6);
			String[] lectureArr6 = lactureDateString6.split(",");
			int lectureMonth6 = Integer.parseInt(lectureArr6[0]);
			int lectureDay6 = Integer.parseInt(lectureArr6[1]);
			System.out.println(formatter.formatLectureList(cathedra.getTTForDay(student6, lectureDay6, lectureMonth6)));
			break;
		case 7:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers7 = sortTeachersByLastName(cathedra.getTeachers());
			System.out.println(formatter.formatTeacherList(sortedTeachers7));
			String teacherNumber7 = reader.readLine();
			exitCheck(teacherNumber7);
			Teacher teacher7 = sortedTeachers7.get(Integer.parseInt(teacherNumber7) - 1);
			System.out.println("Enter date separated by commas without spaces (MONTH,DAY):");
			String lectureDateString7 = reader.readLine();
			exitCheck(lectureDateString7);
			String[] lectureArr7 = lectureDateString7.split(",");
			int lectureMonth7 = Integer.parseInt(lectureArr7[0]);
			int lectureDay7 = Integer.parseInt(lectureArr7[1]);
			System.out.println(formatter.formatLectureList(cathedra.getTTForDay(teacher7, lectureDay7, lectureMonth7)));
			break;
		case 8:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select student from the list:");
			List<Student> students8 = new ArrayList<>();
			cathedra.getGroups().stream().forEach(group -> group.getStudents().stream()
					.filter(student -> !students8.contains(student)).forEach(student -> students8.add(student)));
			List<Student> sortedStudents8 = sortStudentsByLastName(students8);
			System.out.println(formatter.formatStudentList(sortedStudents8));
			String studentNumber8 = reader.readLine();
			exitCheck(studentNumber8);
			Student student8 = sortedStudents8.get(Integer.parseInt(studentNumber8) - 1);
			System.out.println("Enter month number:");
			String lectureMonth8 = reader.readLine();
			exitCheck(lectureMonth8);
			System.out.println(
					formatter.formatLectureList(cathedra.getTTForMonth(student8, Integer.parseInt(lectureMonth8))));
			break;
		case 9:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers9 = sortTeachersByLastName(cathedra.getTeachers());
			System.out.println(formatter.formatTeacherList(sortedTeachers9));
			String teacherNumber9 = reader.readLine();
			exitCheck(teacherNumber9);
			Teacher teacher9 = sortedTeachers9.get(Integer.parseInt(teacherNumber9) - 1);
			System.out.println("Enter month number:");
			String lectureMonth9 = reader.readLine();
			exitCheck(lectureMonth9);
			System.out.println(
					formatter.formatLectureList(cathedra.getTTForMonth(teacher9, Integer.parseInt(lectureMonth9))));
			break;
		case 10:
			System.out.println(formatter.formatAudienceList(sortAudiencesByNumber(cathedra.getAudiences())));
			break;
		case 0:
			break;
		}
	}

	private void submenuUpdate() throws IOException {
		System.out.println(printUpdateMenu());
		int choise = getInput(7);
		switch (choise) {
		case 1:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select student from the list:");
			List<Student> students1 = new ArrayList<>();
			cathedra.getGroups().stream().forEach(group -> group.getStudents().stream()
					.filter(student -> !students1.contains(student)).forEach(student -> students1.add(student)));
			List<Student> sortedStudents1 = sortStudentsByLastName(students1);
			System.out.println(formatter.formatStudentList(sortStudentsByLastName(students1)));
			String studentNumber1 = reader.readLine();
			exitCheck(studentNumber1);
			Student student1 = sortedStudents1.get(Integer.parseInt(studentNumber1) - 1);
			System.out.println("Select group from the list:");
			List<Group> sortedGroups1 = sortGroupsByName(cathedra.getGroups());
			System.out.println(formatter.formatGroupList(sortedGroups1));
			String groupNumber1 = reader.readLine();
			exitCheck(groupNumber1);
			Group group1 = sortedGroups1.get(Integer.parseInt(groupNumber1) - 1);
			student1.setGroup(group1);
			group1.getStudents().add(student1);
			System.out.println("Student group was changed!");
			break;
		case 2:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers2 = sortTeachersByLastName(cathedra.getTeachers());
			System.out.println(formatter.formatTeacherList(sortedTeachers2));
			String teacherNumber2 = reader.readLine();
			exitCheck(teacherNumber2);
			Teacher teacher2 = sortedTeachers2.get(Integer.parseInt(teacherNumber2) - 1);
			System.out.println("Enter vacation start date separated by commas without spaces (YEAR,MONTH,DAY):");
			String vacationStartDateString2 = reader.readLine();
			exitCheck(vacationStartDateString2);
			String[] vacationStartArr2 = vacationStartDateString2.split(",");
			int vacationStartYear2 = Integer.parseInt(vacationStartArr2[0]);
			int vacationStartMonth2 = Integer.parseInt(vacationStartArr2[1]);
			int vacationStartDay2 = Integer.parseInt(vacationStartArr2[2]);
			LocalDate vacationStartDate2 = LocalDate.of(vacationStartYear2, vacationStartMonth2, vacationStartDay2);
			System.out.println("Enter vacation end date separated by commas without spaces (YEAR,MONTH,DAY):");
			String vacationEndDateString2 = reader.readLine();
			exitCheck(vacationEndDateString2);
			String[] vacationEndArr2 = vacationEndDateString2.split(",");
			int vacationEndYear2 = Integer.parseInt(vacationEndArr2[0]);
			int vacationEndMonth2 = Integer.parseInt(vacationEndArr2[1]);
			int vacationEndDay2 = Integer.parseInt(vacationEndArr2[2]);
			LocalDate vacationEndDate2 = LocalDate.of(vacationEndYear2, vacationEndMonth2, vacationEndDay2);
			System.out.println("Enter vacation name:");
			String vacationName2 = reader.readLine();
			exitCheck(vacationName2);
			Vacation vacation2 = new Vacation(vacationName2, vacationStartDate2, vacationEndDate2);
			teacher2.getVacations().add(vacation2);
			System.out.println("Vacation added!");
			break;
		case 3:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Set subject from the list:");
			List<Subject> sortedSubjects3 = sortSubjectsByName(cathedra.getSubjects());
			System.out.println(formatter.formatSubjectList(sortedSubjects3));
			String subjectNumber3 = reader.readLine();
			exitCheck(subjectNumber3);
			Subject subject3 = sortedSubjects3.get(Integer.parseInt(subjectNumber3) - 1);
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers3 = sortTeachersByLastName(cathedra.getTeachers());
			System.out.println(formatter.formatTeacherList(sortedTeachers3));
			String teacherNumber3 = reader.readLine();
			exitCheck(teacherNumber3);
			Teacher teacher3 = sortedTeachers3.get(Integer.parseInt(teacherNumber3) - 1);
			subject3.getTeachers().add(teacher3);
			teacher3.getSubjects().add(subject3);
			System.out.println("Subject was set!");
			break;
		case 4:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select lecture from the list:");
			List<Lecture> sortedLectures4 = sortLecturesByDate(cathedra.getLectures());
			System.out.println(formatter.formatLectureList(sortedLectures4));
			String lectureNumber4 = reader.readLine();
			exitCheck(lectureNumber4);
			Lecture lecture4 = sortedLectures4.get(Integer.parseInt(lectureNumber4) - 1);
			System.out.println("Select audience from the list:");
			List<Audience> sortedAudiences4 = sortAudiencesByNumber(cathedra.getAudiences());
			System.out.println(formatter.formatAudienceList(sortedAudiences4));
			String audienceNumber4 = reader.readLine();
			exitCheck(audienceNumber4);
			Audience audience4 = sortedAudiences4.get(Integer.parseInt(audienceNumber4) - 1);
			lecture4.setAudience(audience4);
			System.out.println("Lecture audience was changed!");
			break;
		case 5:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select lecture from the list:");
			List<Lecture> sortedLectures5 = sortLecturesByDate(cathedra.getLectures());
			System.out.println(formatter.formatLectureList(sortedLectures5));
			String lectureNumber5 = reader.readLine();
			exitCheck(lectureNumber5);
			Lecture lecture5 = sortedLectures5.get(Integer.parseInt(lectureNumber5) - 1);
			System.out.println("Enter the lecture date separated by commas without spaces (YEAR,MONTH,DAY):");
			String lectureDateString5 = reader.readLine();
			exitCheck(lectureDateString5);
			String[] lectureArr5 = lectureDateString5.split(",");
			int lectureYear5 = Integer.parseInt(lectureArr5[0]);
			int lectureMonth5 = Integer.parseInt(lectureArr5[1]);
			int lectureDay5 = Integer.parseInt(lectureArr5[2]);
			LocalDate lectureDate5 = LocalDate.of(lectureYear5, lectureMonth5, lectureDay5);
			lecture5.setDate(lectureDate5);
			System.out.println("Lecture date was changed!");
			break;
		case 6:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select lecture from the list:");
			List<Lecture> sortedLectures6 = sortLecturesByDate(cathedra.getLectures());
			System.out.println(formatter.formatLectureList(sortedLectures6));
			String lectureNumber6 = reader.readLine();
			exitCheck(lectureNumber6);
			Lecture lecture6 = sortedLectures6.get(Integer.parseInt(lectureNumber6) - 1);
			System.out.println("Set lecture time from the list:");
			// TODO: change to dependency on entity
			System.out.println("1. " + LocalTime.of(8, 0) + " - " + LocalTime.of(9, 30));
			System.out.println("2. " + LocalTime.of(9, 40) + " - " + LocalTime.of(11, 10));
			System.out.println("3. " + LocalTime.of(11, 20) + " - " + LocalTime.of(12, 50));
			System.out.println("4. " + LocalTime.of(13, 20) + " - " + LocalTime.of(14, 50));
			System.out.println("5. " + LocalTime.of(15, 0) + " - " + LocalTime.of(16, 30));
			System.out.println("6. " + LocalTime.of(16, 40) + " - " + LocalTime.of(18, 10));
			System.out.println("7. " + LocalTime.of(18, 20) + " - " + LocalTime.of(19, 50));
			System.out.println("8. " + LocalTime.of(20, 0) + " - " + LocalTime.of(21, 30));
			String lectureTimeString6 = reader.readLine();
			exitCheck(lectureTimeString6);
			// TODO: change to entity
			LectureTime lectureTime6 = dataCreator.createLectureTime(Integer.parseInt(lectureTimeString6));
			lecture6.setTime(lectureTime6);
			System.out.println("Lecture time was changed!");
			break;
		case 7:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select lecture from the list:");
			List<Lecture> sortedLectures7 = sortLecturesByDate(cathedra.getLectures());
			System.out.println(formatter.formatLectureList(sortedLectures7));
			String lectureNumber7 = reader.readLine();
			exitCheck(lectureNumber7);
			Lecture lecture7 = sortedLectures7.get(Integer.parseInt(lectureNumber7) - 1);
			System.out.println("Select group from the list:");
			List<Group> sortedGroups7 = sortGroupsByName(cathedra.getGroups());
			System.out.println(formatter.formatGroupList(sortedGroups7));
			String groupNumber7 = reader.readLine();
			exitCheck(groupNumber7);
			Group group7 = sortedGroups7.get(Integer.parseInt(groupNumber7) - 1);
			lecture7.getGroups().add(group7);
			group7.getLectures().add(lecture7);
			System.out.println("Lecture was set to group!");
			break;
		case 0:
			break;
		}
	}

	private void submenuDelete() throws IOException {
		System.out.println(printDeleteMenu());
		int choise = getInput(8);
		switch (choise) {
		case 1:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select student from the list:");
			List<Student> students1 = new ArrayList<>();
			cathedra.getGroups().stream().forEach(group -> group.getStudents().stream()
					.filter(student -> !students1.contains(student)).forEach(student -> students1.add(student)));
			List<Student> sortedStudents1 = sortStudentsByLastName(students1);
			System.out.println(formatter.formatStudentList(sortedStudents1));
			String studentNumber1 = reader.readLine();
			exitCheck(studentNumber1);
			Student student1 = sortedStudents1.get(Integer.parseInt(studentNumber1) - 1);
			cathedra.getGroups().stream().filter(group -> group.getStudents().contains(student1))
					.forEach(group -> group.getStudents().remove(student1));
			System.out.println("Student was deleted!");
			break;
		case 2:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers2 = sortTeachersByLastName(cathedra.getTeachers());
			System.out.println(formatter.formatTeacherList(sortedTeachers2));
			String teacherNumber2 = reader.readLine();
			exitCheck(teacherNumber2);
			Teacher teacher2 = sortedTeachers2.get(Integer.parseInt(teacherNumber2) - 1);
			cathedra.getTeachers().remove(teacher2);
			cathedra.getSubjects().stream().filter(subject -> subject.getTeachers().contains(teacher2))
					.forEach(subject -> subject.getTeachers().remove(teacher2));
			System.out.println("Teacher was deleted!");
			break;
		case 3:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select group from the list:");
			List<Group> sortedGroups3 = sortGroupsByName(cathedra.getGroups());
			System.out.println(formatter.formatGroupList(sortedGroups3));
			String groupNumber3 = reader.readLine();
			exitCheck(groupNumber3);
			Group group3 = sortedGroups3.get(Integer.parseInt(groupNumber3) - 1);
			if (!group3.getStudents().isEmpty()) {
				System.out.println("Please remove students first from group!");
			} else {
				cathedra.getLectures().stream().filter(lecture -> lecture.getGroups().contains(group3))
						.forEach(lecture -> lecture.getGroups().remove(group3));
				cathedra.getGroups().remove(group3);
				System.out.println("Group was deleted!");
			}
			break;
		case 4:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select lecture from the list:");
			List<Lecture> sortedLectures4 = sortLecturesByDate(cathedra.getLectures());
			System.out.println(formatter.formatLectureList(sortedLectures4));
			String lectureNumber4 = reader.readLine();
			exitCheck(lectureNumber4);
			Lecture lecture7 = sortedLectures4.get(Integer.parseInt(lectureNumber4) - 1);
			cathedra.getGroups().stream().filter(group -> group.getLectures().contains(lecture7))
					.forEach(group -> group.getLectures().remove(lecture7));
			cathedra.getLectures().remove(lecture7);
			System.out.println("Lecture was deleted!");
			break;
		case 5:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select audience from the list:");
			List<Audience> sortedAudiences5 = sortAudiencesByNumber(cathedra.getAudiences());
			System.out.println(formatter.formatAudienceList(sortedAudiences5));
			String audienceNumber5 = reader.readLine();
			exitCheck(audienceNumber5);
			Audience audience5 = sortedAudiences5.get(Integer.parseInt(audienceNumber5) - 1);
			boolean checker5 = cathedra.getLectures().stream()
					.anyMatch(lecture -> lecture.getAudience().equals(audience5));
			if (checker5) {
				System.out.println("Please remove audiences first from lectures!");
			} else {
				cathedra.getAudiences().remove(audience5);
				System.out.println("Audience was deleted!");
			}
			break;
		case 6:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select subject from the list:");
			List<Subject> sortedSubjects6 = sortSubjectsByName(cathedra.getSubjects());
			System.out.println(formatter.formatSubjectList(sortedSubjects6));
			String subjectNumber6 = reader.readLine();
			exitCheck(subjectNumber6);
			Subject subject6 = sortedSubjects6.get(Integer.parseInt(subjectNumber6) - 1);
			boolean checker6 = cathedra.getLectures().stream()
					.anyMatch(lecture -> lecture.getSubject().equals(subject6));
			if (checker6) {
				System.out.println("Please remove subject first from lecture!");
			} else {
				cathedra.getSubjects().remove(subject6);
				cathedra.getTeachers().stream().forEach(teacher -> teacher.getSubjects().remove(subject6));
				System.out.println("Subject was deleted!");
			}
			break;
		case 7:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers7 = sortTeachersByLastName(cathedra.getTeachers());
			System.out.println(formatter.formatTeacherList(sortedTeachers7));
			String teacherNumber7 = reader.readLine();
			exitCheck(teacherNumber7);
			Teacher teacher7 = sortedTeachers7.get(Integer.parseInt(teacherNumber7) - 1);
			System.out.println("Select vacation from the list:");
			List<Vacation> sortedVacations7 = sortVacationsByDate(teacher7.getVacations());
			System.out.println(formatter.formatVacationList(sortedVacations7));
			String vacationNumber7 = reader.readLine();
			exitCheck(vacationNumber7);
			Vacation vacation7 = sortedVacations7.get(Integer.parseInt(vacationNumber7) - 1);
			teacher7.getVacations().remove(vacation7);
			System.out.println("Teacher vacation was deleted!");
			break;
		case 8:
			System.out.println("If you want to cancel, type 0 at any stage");
			System.out.println("Select holiday from the list:");
			List<Holiday> sortedHolidays8 = sortHolidaysByDate(cathedra.getHolidays());
			System.out.println(formatter.formatHolidayList(sortedHolidays8));
			String holidayNumber8 = reader.readLine();
			exitCheck(holidayNumber8);
			Holiday holiday = sortedHolidays8.get(Integer.parseInt(holidayNumber8) - 1);
			cathedra.getHolidays().remove(holiday);
			System.out.println("Holiday was deleted!");
			break;
		case 0:
			break;
		}
	}

	private List<Student> sortStudentsByLastName(List<Student> list) {
		return list.stream().sorted((d1, d2) -> d1.getLastName().compareTo(d2.getLastName()))
				.collect(Collectors.toList());
	}

	private List<Teacher> sortTeachersByLastName(List<Teacher> list) {
		return list.stream().sorted((d1, d2) -> d1.getLastName().compareTo(d2.getLastName()))
				.collect(Collectors.toList());
	}

	private List<Subject> sortSubjectsByName(List<Subject> list) {
		return list.stream().sorted((d1, d2) -> d1.getName().compareTo(d2.getName())).collect(Collectors.toList());
	}

	private List<Lecture> sortLecturesByDate(List<Lecture> list) {
		return list.stream().sorted((d1, d2) -> d1.getDate().compareTo(d2.getDate())).collect(Collectors.toList());
	}

	private List<Group> sortGroupsByName(List<Group> list) {
		return list.stream().sorted((d1, d2) -> d1.getName().compareTo(d2.getName())).collect(Collectors.toList());
	}

	private List<Audience> sortAudiencesByNumber(List<Audience> list) {
		return list.stream().sorted((d1, d2) -> Integer.compare(d1.getRoom(), d2.getRoom()))
				.collect(Collectors.toList());
	}

	private List<Holiday> sortHolidaysByDate(List<Holiday> list) {
		return list.stream().sorted((d1, d2) -> d1.getDate().compareTo(d2.getDate())).collect(Collectors.toList());
	}

	private List<Vacation> sortVacationsByDate(List<Vacation> list) {
		return list.stream().sorted((d1, d2) -> d1.getStart().compareTo(d2.getStart())).collect(Collectors.toList());
	}

	private void exitCheck(String arg) {
		if (arg.equals("0")) {
			System.out.println("Exit completed!");
			System.exit(0);
		}
	}
}

package com.foxminded.university;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.foxminded.university.config.SpringConfig;
import com.foxminded.university.service.AudienceService;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.HolidayService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.LectureTimeService;
import com.foxminded.university.service.StudentService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.VacationService;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;

public class MenuCreator {
	
	private static final Logger logger = LoggerFactory.getLogger(MenuCreator.class);

	private final ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
	private boolean exit;
	private BufferedReader reader;
	private Formatter formatter = new Formatter();
	private AudienceService audienceService = context.getBean(AudienceService.class);
	private CathedraService cathedraService = context.getBean(CathedraService.class);
	private GroupService groupService = context.getBean(GroupService.class);
	private StudentService studentService = context.getBean(StudentService.class);
	private SubjectService subjectService = context.getBean(SubjectService.class);
	private TeacherService teacherService = context.getBean(TeacherService.class);
	private LectureService lectureService = context.getBean(LectureService.class);
	private LectureTimeService lectureTimeService = context.getBean(LectureTimeService.class);
	private HolidayService holidayService = context.getBean(HolidayService.class);
	private VacationService vacationService = context.getBean(VacationService.class);

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
		menu.append("9  - Delete group from lecture");
		menu.append(System.lineSeparator());
		menu.append("10  - Delete subject from teacher");
		menu.append(System.lineSeparator());
		menu.append("0  - Go to the main menu");

		return menu.toString();
	}

	private int getInput(int max) {
		int choise = -1;
		while (true) {
			try {
				choise = Integer.parseInt(reader.readLine());
				if (choise < 0 || choise > max) {
					logger.debug("Input not in max range");
					System.out.println("Invalid selection. Please try again");
				} else {
					break;
				}
			} catch (NumberFormatException | IOException e) {
				logger.debug("Not an integer input");
				System.out.println("Invalid selection. Please try again");
			}
		}
		return choise;
	}

	private void exitCheck(String input) {
		if (input.equals("0") | input.isEmpty()) {
			System.out.println("Exit completed!");
			System.exit(0);
		}
	}

	private LocalDate setupLocalDate() {
		LocalDate date;
		while (true) {
			try {
				String input = reader.readLine();
				exitCheck(input);
				String[] splittedArray = input.split(",");
				if (splittedArray.length != 3) {
					throw new ArrayIndexOutOfBoundsException();
				}
				int year = Integer.parseInt(splittedArray[0]);
				int month = Integer.parseInt(splittedArray[1]);
				int day = Integer.parseInt(splittedArray[2]);
				date = LocalDate.of(year, month, day);
				break;
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException | DateTimeException | IOException e) {
				logger.debug("Bad date input");
				System.out.println("Invalid selection. Please try again");
			}
		}
		return date;
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
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
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
			System.out.println(formatter.getGenderString());
			Gender gender1 = genderMaker();
			System.out.println("Enter student postal code:");
			String postalCode1 = reader.readLine();
			exitCheck(postalCode1);
			System.out.println("Enter student education level:");
			String education1 = reader.readLine();
			exitCheck(education1);
			System.out.println("Enter the date of birth separated by commas without spaces (YEAR,MONTH,DAY):");
			LocalDate birthDate1 = setupLocalDate();
			Student student = Student.builder()
					.firstName(firstName1)
					.lastName(lastName1)
					.address(address1)
					.gender(gender1)
					.birthDate(birthDate1)
					.phone(phone1)
					.email(email1)
					.postalCode(postalCode1)
					.education(education1)
					.build();
			studentService.save(student);
			break;
		case 2:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
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
			System.out.println(formatter.getGenderString());
			Gender gender2 = genderMaker();
			System.out.println("Enter teacher postal code:");
			String postalCode2 = reader.readLine();
			exitCheck(postalCode2);
			System.out.println("Enter teacher education level:");
			String education2 = reader.readLine();
			exitCheck(education2);
			System.out.println("Enter the date of birth separated by commas without spaces (YEAR,MONTH,DAY):");
			LocalDate birthDate2 = setupLocalDate();
			System.out.println("Enter teacher degree:");
			System.out.println(formatter.getDegreeString());
			Degree degree2 = degreeMaker();
			System.out.println("Set subjects from list separated by commas without spaces (SUBJ or SUBJ,SUBJ,SUBJ):");
			List<Subject> sortedSubjects2 = sortSubjectsByName(subjectService.findAll());
			System.out.println(formatter.formatSubjectList(sortedSubjects2));
			List<Subject> subjects2 = new ArrayList<>();
			while (true) {
				try {
					String subjectNumber2 = reader.readLine();
					exitCheck(subjectNumber2);
					String[] subjectsArr2 = subjectNumber2.split(",");
					for (String subjNumString : subjectsArr2) {
						int subjNum = Integer.parseInt(subjNumString);
						if (subjNum < 0 | subjNum > sortedSubjects2.size()) {
							throw new ArrayIndexOutOfBoundsException();
						}
						subjects2.add(sortedSubjects2.get(subjNum - 1));
					}
					break;
				} catch (NumberFormatException | ArrayIndexOutOfBoundsException | IOException e) {
					System.out.println("Invalid selections. Please try again");
				}
			}
			Teacher teacher = Teacher.builder()
					.firstName(firstName2)
					.lastName(lastName2)
					.address(address2)
					.gender(gender2)
					.birthDate(birthDate2)
					.cathedra(cathedraService.findById(1))
					.degree(degree2)
					.phone(phone2)
					.email(email2)
					.postalCode(postalCode2)
					.education(education2)
					.build();
			for (Subject subject : subjects2) {
				if (!teacher.getSubjects().contains(subject)) {
					teacher.getSubjects().add(subject);
				}
			}
			teacherService.save(teacher);
			break;
		case 3:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Enter subject name:");
			String subjectName3 = reader.readLine();
			exitCheck(subjectName3);
			System.out.println("Enter subject description:");
			String subjectDescription3 = reader.readLine();
			exitCheck(subjectDescription3);
			subjectService.save(
					Subject.builder()
					.cathedra(cathedraService.findById(1))
					.name(subjectName3)
					.description(subjectDescription3)
					.build());
			break;
		case 4:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Enter group name:");
			String groupName4 = reader.readLine();
			exitCheck(groupName4);
			groupService.save(
					Group.builder()
					.name(groupName4)
					.cathedra(cathedraService.findById(1))
					.build());
			break;
		case 5:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Set subject from the list:");
			List<Subject> sortedSubjects5 = sortSubjectsByName(subjectService.findAll());
			System.out.println(formatter.formatSubjectList(sortedSubjects5));
			int subjectNumber5 = getInput(sortedSubjects5.size());
			exitCheck(String.valueOf(subjectNumber5));
			Subject subject5 = sortedSubjects5.get(subjectNumber5 - 1);
			System.out.println("Set teacher from the list:");
			List<Teacher> sortedTeachers5 = sortTeachersByLastName(teacherService.findAll());
			System.out.println(formatter.formatTeacherList(sortedTeachers5));
			int teacherNumber5 = getInput(sortedTeachers5.size());
			exitCheck(String.valueOf(teacherNumber5));
			Teacher teacher5 = sortedTeachers5.get(teacherNumber5 - 1);
			System.out.println("Set audience from the list:");
			List<Audience> sortedAudiences = sortAudiencesByNumber(audienceService.findAll());
			System.out.println(formatter.formatAudienceList(sortedAudiences));
			int audienceNumber5 = getInput(sortedAudiences.size());
			exitCheck(String.valueOf(audienceNumber5));
			Audience audience5 = sortedAudiences.get(audienceNumber5 - 1);
			System.out.println("Enter the lecture date separated by commas without spaces (YEAR,MONTH,DAY):");
			LocalDate lectureDate5 = setupLocalDate();
			System.out.println("Set lecture time from the list:");
			List<LectureTime> sortedLectureTimes5 = sortLectureTimesByTime(lectureTimeService.findAll());
			System.out.println(formatter.formatLectureTimesList(sortedLectureTimes5));
			int lectureTimeNumber5 = getInput(sortedLectureTimes5.size());
			exitCheck(String.valueOf(lectureTimeNumber5));
			LectureTime lectureTime5 = sortedLectureTimes5.get(lectureTimeNumber5 - 1);
			lectureService.save(
					Lecture.builder()
					.cathedra(cathedraService.findById(1))
					.subject(subject5)
					.date(lectureDate5)
					.time(lectureTime5)
					.audience(audience5)
					.teacher(teacher5)
					.build());
			break;
		case 6:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Enter holiday description");
			String holidayDescription6 = reader.readLine();
			exitCheck(holidayDescription6);
			System.out.println("Enter the holiday date separated by commas without spaces (YEAR,MONTH,DAY):");
			LocalDate holidayDate5 = setupLocalDate();
			System.out.println("Set cathedra from list:");

			List<Cathedra> sortedCathedras6 = sortCathedrasByName(cathedraService.findAll());
			System.out.println(formatter.formatCathedraList(sortedCathedras6));
			int cathedraNumber6 = getInput(sortedCathedras6.size());
			exitCheck(String.valueOf(cathedraNumber6));
			Cathedra cathedra6 = sortedCathedras6.get(cathedraNumber6 - 1);
			Holiday holiday6 = Holiday.builder()
					.name(holidayDescription6)
					.date(holidayDate5)
					.cathedra(cathedra6)
					.build();
			holidayService.save(holiday6);
			break;
		case 7:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Enter audience room");
			String audienceRoom7 = reader.readLine();
			exitCheck(audienceRoom7);
			System.out.println("Enter audience capacity");
			String audienceCapacity7 = reader.readLine();
			exitCheck(audienceCapacity7);
			Audience audience7 = Audience.builder()
					.room(Integer.parseInt(audienceRoom7))
					.capacity(Integer.parseInt(audienceCapacity7))
					.cathedra(cathedraService.findById(1))
					.build();
			audienceService.save(audience7);
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
			System.out.println(formatter.formatStudentList(sortStudentsByLastName(studentService.findAll())));
			break;
		case 2:
			System.out.println(formatter.formatTeacherList(sortTeachersByLastName(teacherService.findAll())));
			break;
		case 3:
			System.out.println(formatter.formatSubjectList(sortSubjectsByName(subjectService.findAll())));
			break;
		case 4:
			System.out.println(formatter.formatGroupList(sortGroupsByName(groupService.findAll())));
			break;
		case 5:
			System.out.println(formatter.formatHolidayList(sortHolidaysByDate(holidayService.findAll())));
			break;
		case 6:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select student from the list:");
			List<Student> sortedStudents6 = sortStudentsByLastName(studentService.findAll());
			System.out.println(formatter.formatStudentList(sortedStudents6));
			int studentNumber6 = getInput(sortedStudents6.size());
			exitCheck(String.valueOf(studentNumber6));
			Student student6 = sortedStudents6.get(studentNumber6 - 1);
			System.out.println("Enter date separated by commas without spaces (MONTH,DAY):");
			while (true) {
				try {
					String lactureDateString6 = reader.readLine();
					exitCheck(lactureDateString6);
					String[] lectureArr6 = lactureDateString6.split(",");
					if (lectureArr6.length != 2) {
						throw new ArrayIndexOutOfBoundsException();
					}
					int lectureMonth6 = Integer.parseInt(lectureArr6[0]);
					int lectureDay6 = Integer.parseInt(lectureArr6[1]);
					MonthDay date = MonthDay.of(lectureMonth6, lectureDay6);
					System.out.println(formatter.formatLectureList(getTTForDay(student6, date)));
					break;
				} catch (NumberFormatException | DateTimeException | ArrayIndexOutOfBoundsException | IOException e) {
					System.out.println("Invalid selection. Please try again");
				}
			}
			break;

		case 7:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers7 = sortTeachersByLastName(teacherService.findAll());
			System.out.println(formatter.formatTeacherList(sortedTeachers7));
			int teacherNumber7 = getInput(sortedTeachers7.size());
			exitCheck(String.valueOf(teacherNumber7));
			Teacher teacher7 = sortedTeachers7.get(teacherNumber7 - 1);
			System.out.println("Enter date separated by commas without spaces (MONTH,DAY):");
			while (true) {
				try {
					String lectureDateString7 = reader.readLine();
					exitCheck(lectureDateString7);
					String[] lectureArr7 = lectureDateString7.split(",");
					if (lectureArr7.length != 2) {
						throw new ArrayIndexOutOfBoundsException();
					}
					int lectureMonth7 = Integer.parseInt(lectureArr7[0]);
					int lectureDay7 = Integer.parseInt(lectureArr7[1]);
					MonthDay date7 = MonthDay.of(lectureMonth7, lectureDay7);
					System.out.println(formatter.formatLectureList(getTTForDay(teacher7, date7)));
					break;
				} catch (NumberFormatException | DateTimeException | ArrayIndexOutOfBoundsException | IOException e) {
					System.out.println("Invalid selection. Please try again");
				}
			}
			break;
		case 8:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select student from the list:");
			List<Student> sortedStudents8 = sortStudentsByLastName(studentService.findAll());
			System.out.println(formatter.formatStudentList(sortedStudents8));
			int studentNumber8 = getInput(sortedStudents8.size());
			exitCheck(String.valueOf(studentNumber8));
			Student student8 = sortedStudents8.get(studentNumber8 - 1);
			System.out.println("Enter month number:");
			while (true) {
				try {
					String lectureMonth8 = reader.readLine();
					exitCheck(lectureMonth8);
					Month date = Month.of(Integer.parseInt(lectureMonth8));
					System.out.println(formatter.formatLectureList(getTTForMonth(student8, date)));
					break;
				} catch (NumberFormatException | DateTimeException | ArrayIndexOutOfBoundsException | IOException e) {
					System.out.println("Invalid selection. Please try again");
				}
			}
			break;
		case 9:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers9 = sortTeachersByLastName(teacherService.findAll());
			System.out.println(formatter.formatTeacherList(sortedTeachers9));
			int teacherNumber9 = getInput(sortedTeachers9.size());
			exitCheck(String.valueOf(teacherNumber9));
			Teacher teacher9 = sortedTeachers9.get(teacherNumber9 - 1);
			System.out.println("Enter month number:");
			while (true) {
				try {
					String lectureMonth9 = reader.readLine();
					exitCheck(lectureMonth9);
					Month date9 = Month.of(Integer.parseInt(lectureMonth9));
					System.out.println(formatter.formatLectureList(getTTForMonth(teacher9, date9)));
					break;
				} catch (NumberFormatException | DateTimeException | ArrayIndexOutOfBoundsException | IOException e) {
					System.out.println("Invalid selection. Please try again");
				}
			}
			break;
		case 10:
			System.out.println(formatter.formatAudienceList(sortAudiencesByNumber(audienceService.findAll())));
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
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select student from the list:");
			List<Student> sortedStudents1 = sortStudentsByLastName(studentService.findAll());
			System.out.println(formatter.formatStudentList(sortedStudents1));
			int studentNumber1 = getInput(sortedStudents1.size());
			exitCheck(String.valueOf(studentNumber1));
			Student student1 = sortedStudents1.get(studentNumber1 - 1);
			System.out.println("Select group from the list:");
			List<Group> sortedGroups1 = sortGroupsByName(groupService.findAll());
			System.out.println(formatter.formatGroupList(sortedGroups1));
			int groupNumber1 = getInput(sortedGroups1.size());
			exitCheck(String.valueOf(groupNumber1));
			Group group1 = sortedGroups1.get(groupNumber1 - 1);
			student1.setGroup(group1);
			studentService.save(student1);
			break;
		case 2:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers2 = sortTeachersByLastName(teacherService.findAll());
			System.out.println(formatter.formatTeacherList(sortedTeachers2));
			int teacherNumber2 = getInput(sortedTeachers2.size());
			exitCheck(String.valueOf(teacherNumber2));
			Teacher teacher2 = sortedTeachers2.get(teacherNumber2 - 1);
			System.out.println("Enter vacation start date separated by commas without spaces (YEAR,MONTH,DAY):");
			LocalDate vacationStartDate2 = setupLocalDate();
			System.out.println("Enter vacation end date separated by commas without spaces (YEAR,MONTH,DAY):");
			LocalDate vacationEndDate2 = setupLocalDate();
			Vacation vacation2 = Vacation.builder()
					.start(vacationStartDate2)
					.end(vacationEndDate2)
					.teacher(teacher2)
					.build();
			vacationService.save(vacation2);
			break;
		case 3:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Set subject from the list:");
			List<Subject> sortedSubjects3 = sortSubjectsByName(subjectService.findAll());
			System.out.println(formatter.formatSubjectList(sortedSubjects3));
			int subjectNumber3 = getInput(sortedSubjects3.size());
			exitCheck(String.valueOf(subjectNumber3));
			Subject subject3 = sortedSubjects3.get(subjectNumber3 - 1);
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers3 = sortTeachersByLastName(teacherService.findAll());
			System.out.println(formatter.formatTeacherList(sortedTeachers3));
			int teacherNumber3 = getInput(sortedTeachers3.size());
			exitCheck(String.valueOf(teacherNumber3));
			Teacher teacher3 = sortedTeachers3.get(teacherNumber3 - 1);
			if (!teacher3.getSubjects().contains(subject3)) {
				teacher3.getSubjects().add(subject3);
				teacherService.save(teacher3);
			} else {
				System.out.println("Teacher already have this subject");
			}
			break;
		case 4:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select lecture from the list:");
			List<Lecture> test = lectureService.findAll();
			List<Lecture> sortedLectures4 = sortLecturesByDate(test);
			System.out.println(formatter.formatLectureList(sortedLectures4));
			int lectureNumber4 = getInput(sortedLectures4.size());
			exitCheck(String.valueOf(lectureNumber4));
			Lecture lecture4 = sortedLectures4.get(lectureNumber4 - 1);
			System.out.println("Select audience from the list:");
			List<Audience> sortedAudiences4 = sortAudiencesByNumber(audienceService.findAll());
			System.out.println(formatter.formatAudienceList(sortedAudiences4));
			int audienceNumber4 = getInput(sortedAudiences4.size());
			exitCheck(String.valueOf(audienceNumber4));
			Audience audience4 = sortedAudiences4.get(audienceNumber4 - 1);
			lecture4.setAudience(audience4);
			lectureService.save(lecture4);
			break;
		case 5:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select lecture from the list:");
			List<Lecture> sortedLectures5 = sortLecturesByDate(lectureService.findAll());
			System.out.println(formatter.formatLectureList(sortedLectures5));
			int lectureNumber5 = getInput(sortedLectures5.size());
			exitCheck(String.valueOf(lectureNumber5));
			Lecture lecture5 = sortedLectures5.get(lectureNumber5 - 1);
			System.out.println("Enter the lecture date separated by commas without spaces (YEAR,MONTH,DAY):");
			lecture5.setDate(setupLocalDate());
			lectureService.save(lecture5);
			break;
		case 6:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select lecture from the list:");
			List<Lecture> sortedLectures6 = sortLecturesByDate(lectureService.findAll());
			System.out.println(formatter.formatLectureList(sortedLectures6));
			int lectureNumber6 = getInput(sortedLectures6.size());
			exitCheck(String.valueOf(lectureNumber6));
			Lecture lecture6 = sortedLectures6.get(lectureNumber6 - 1);
			System.out.println("Set lecture time from the list:");
			List<LectureTime> sortedLectureTimes6 = sortLectureTimesByTime(lectureTimeService.findAll());
			System.out.println(formatter.formatLectureTimesList(sortedLectureTimes6));
			int lectureTimeNumber6 = getInput(sortedLectureTimes6.size());
			exitCheck(String.valueOf(lectureTimeNumber6));
			LectureTime lectureTime6 = sortedLectureTimes6.get(lectureTimeNumber6 - 1);
			lecture6.setTime(lectureTime6);
			lectureService.save(lecture6);
			break;
		case 7:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select lecture from the list:");
			List<Lecture> sortedLectures7 = sortLecturesByDate(lectureService.findAll());
			System.out.println(formatter.formatLectureList(sortedLectures7));
			int lectureNumber7 = getInput(sortedLectures7.size());
			exitCheck(String.valueOf(lectureNumber7));
			Lecture lecture7 = sortedLectures7.get(lectureNumber7 - 1);
			System.out.println("Select group from the list:");
			List<Group> sortedGroups7 = sortGroupsByName(groupService.findAll());
			System.out.println(formatter.formatGroupList(sortedGroups7));
			int groupNumber7 = getInput(sortedGroups7.size());
			exitCheck(String.valueOf(groupNumber7));
			Group group7 = sortedGroups7.get(groupNumber7 - 1);
			if (!lecture7.getGroups().contains(group7)) {
				lecture7.getGroups().add(group7);
				lectureService.save(lecture7);
			} else {
				System.out.println("Lecture already have this group!");
			}
			break;
		case 0:
			break;
		}
	}

	private void submenuDelete() throws IOException {
		System.out.println(printDeleteMenu());
		int choise = getInput(10);
		switch (choise) {
		case 1:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select student from the list:");
			List<Student> sortedStudents1 = sortStudentsByLastName(studentService.findAll());
			System.out.println(formatter.formatStudentList(sortedStudents1));
			int studentNumber1 = getInput(sortedStudents1.size());
			exitCheck(String.valueOf(studentNumber1));
			Student student1 = sortedStudents1.get(studentNumber1 - 1);
			studentService.deleteById(student1.getId());
			break;
		case 2:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers2 = sortTeachersByLastName(teacherService.findAll());
			System.out.println(formatter.formatTeacherList(sortedTeachers2));
			int teacherNumber2 = getInput(sortedTeachers2.size());
			exitCheck(String.valueOf(teacherNumber2));
			Teacher teacher2 = sortedTeachers2.get(teacherNumber2 - 1);
			if (lectureService.findAll().stream().anyMatch(lecture -> lecture.getTeacher().equals(teacher2))) {
				System.out.println("Please remove lectures with this teacher first!");
			} else {
				teacherService.deleteById(teacher2.getId());
			}
			break;
		case 3:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select group from the list:");
			List<Group> sortedGroups3 = sortGroupsByName(groupService.findAll());
			System.out.println(formatter.formatGroupList(sortedGroups3));
			int groupNumber3 = getInput(sortedGroups3.size());
			exitCheck(String.valueOf(groupNumber3));
			Group group3 = sortedGroups3.get(groupNumber3 - 1);
			if (studentService.findAll().stream().anyMatch(student -> student.getGroup().equals(group3))) {
				System.out.println("Please remove students first from group!");
			} else {
				groupService.deleteById(group3.getId());
			}
			break;
		case 4:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select lecture from the list:");
			List<Lecture> sortedLectures4 = sortLecturesByDate(lectureService.findAll());
			System.out.println(formatter.formatLectureList(sortedLectures4));
			int lectureNumber4 = getInput(sortedLectures4.size());
			exitCheck(String.valueOf(lectureNumber4));
			Lecture lecture4 = sortedLectures4.get(lectureNumber4 - 1);
			lectureService.deleteById(lecture4.getId());
			break;
		case 5:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select audience from the list:");
			List<Audience> sortedAudiences5 = sortAudiencesByNumber(audienceService.findAll());
			System.out.println(formatter.formatAudienceList(sortedAudiences5));
			int audienceNumber5 = getInput(sortedAudiences5.size());
			exitCheck(String.valueOf(audienceNumber5));
			Audience audience5 = sortedAudiences5.get(audienceNumber5 - 1);
			if (lectureService.findAll().stream().anyMatch(lecture -> lecture.getAudience().equals(audience5))) {
				System.out.println("Please remove audiences first from lectures!");
			} else {
				audienceService.deleteById(audience5.getId());
			}
			break;
		case 6:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select subject from the list:");
			List<Subject> sortedSubjects6 = sortSubjectsByName(subjectService.findAll());
			System.out.println(formatter.formatSubjectList(sortedSubjects6));
			int subjectNumber6 = getInput(sortedSubjects6.size());
			exitCheck(String.valueOf(subjectNumber6));
			Subject subject6 = sortedSubjects6.get(subjectNumber6 - 1);
			if (lectureService.findAll().stream().anyMatch(lecture -> lecture.getSubject().equals(subject6))) {
				System.out.println("Please remove subject first from lecture!");
			} else {
				subjectService.deleteById(subject6.getId());
			}
			break;
		case 7:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers7 = sortTeachersByLastName(teacherService.findAll());
			System.out.println(formatter.formatTeacherList(sortedTeachers7));
			int teacherNumber7 = getInput(sortedTeachers7.size());
			exitCheck(String.valueOf(teacherNumber7));
			Teacher teacher7 = sortedTeachers7.get(teacherNumber7 - 1);
			System.out.println("Select vacation from the list:");
			List<Vacation> sortedVacations7 = sortVacationsByDate(vacationService.findByTeacherId(teacher7.getId()));
			System.out.println(formatter.formatVacationList(sortedVacations7));
			int vacationNumber7 = getInput(sortedVacations7.size());
			exitCheck(String.valueOf(vacationNumber7));
			Vacation vacation7 = sortedVacations7.get(vacationNumber7 - 1);
			vacationService.deleteById(vacation7.getId());
			break;
		case 8:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select holiday from the list:");
			List<Holiday> sortedHolidays8 = sortHolidaysByDate(holidayService.findAll());
			System.out.println(formatter.formatHolidayList(sortedHolidays8));
			int holidayNumber8 = getInput(sortedHolidays8.size());
			exitCheck(String.valueOf(holidayNumber8));
			Holiday holiday = sortedHolidays8.get(holidayNumber8 - 1);
			holidayService.deleteById(holiday.getId());
			break;
		case 9:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select lecture from the list:");
			List<Lecture> sortedLectures9 = sortLecturesByDate(lectureService.findAll());
			System.out.println(formatter.formatLectureList(sortedLectures9));
			int lectureNumber9 = getInput(sortedLectures9.size());
			exitCheck(String.valueOf(lectureNumber9));
			Lecture lecture9 = sortedLectures9.get(lectureNumber9 - 1);
			if (lecture9.getGroups().isEmpty()) {
				System.out.println("No groups on this lecture!");
				break;
			}
			System.out.println("Select group from the list:");
			List<Group> sortedGroups9 = sortGroupsByName(lecture9.getGroups());
			System.out.println(formatter.formatGroupList(sortedGroups9));
			int groupNumber9 = getInput(sortedGroups9.size());
			exitCheck(String.valueOf(groupNumber9));
			Group group9 = sortedGroups9.get(groupNumber9 - 1);
			lecture9.getGroups().remove(group9);
			lectureService.save(lecture9);
			break;
		case 10:
			System.out.println("If you want to cancel, type 0 or nothing at any stage");
			System.out.println("Select teacher from the list:");
			List<Teacher> sortedTeachers10 = sortTeachersByLastName(teacherService.findAll());
			System.out.println(formatter.formatTeacherList(sortedTeachers10));
			int teacherNumber10 = getInput(sortedTeachers10.size());
			exitCheck(String.valueOf(teacherNumber10));
			Teacher teacher10 = sortedTeachers10.get(teacherNumber10 - 1);
			if (teacher10.getSubjects().isEmpty()) {
				System.out.println("No subjects on this teacher");
				break;
			}
			System.out.println("Select subject from the list:");
			List<Subject> sortedSubjects10 = sortSubjectsByName(teacher10.getSubjects());
			System.out.println(formatter.formatSubjectList(sortedSubjects10));
			int subjectNumber10 = getInput(sortedSubjects10.size());
			exitCheck(String.valueOf(subjectNumber10));
			Subject subject10 = sortedSubjects10.get(subjectNumber10 - 1);
			teacher10.getSubjects().remove(subject10);
			teacherService.save(teacher10);
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
		return list.stream()
				.sorted(Comparator.comparing(Lecture::getDate)
						.thenComparing((d1, d2) -> d1.getTime().getStart().compareTo(d2.getTime().getStart())))
				.collect(Collectors.toList());
	}

	private List<Group> sortGroupsByName(List<Group> list) {
		return list.stream().sorted((d1, d2) -> d1.getName().compareTo(d2.getName())).collect(Collectors.toList());
	}

	private List<Cathedra> sortCathedrasByName(List<Cathedra> list) {
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

	private List<LectureTime> sortLectureTimesByTime(List<LectureTime> list) {
		return list.stream().sorted((d1, d2) -> d1.getStart().compareTo(d2.getStart())).collect(Collectors.toList());
	}

	public List<Lecture> getTTForDay(Student student, MonthDay date) {
		return lectureService.findAll().stream()
				.sorted((d1, d2) -> d1.getTime().getStart().compareTo(d2.getTime().getStart()))
				.filter(lecture -> lecture.getDate().getMonthValue() == date.getMonthValue()
						&& lecture.getDate().getDayOfMonth() == date.getDayOfMonth())
				.filter(lecture -> lecture.getGroups().contains(student.getGroup())).collect(Collectors.toList());
	}

	public List<Lecture> getTTForDay(Teacher teacher, MonthDay date) {
		return lectureService.findAll().stream().sorted((d1, d2) -> d1.getDate().compareTo(d2.getDate()))
				.filter(lecture -> lecture.getTeacher().equals(teacher)
						&& (lecture.getDate().getMonthValue() == date.getMonthValue()
								&& lecture.getDate().getDayOfMonth() == date.getDayOfMonth()))
				.collect(Collectors.toList());
	}

	public List<Lecture> getTTForMonth(Student student, Month date) {
		return lectureService.findAll().stream().sorted((d1, d2) -> d1.getDate().compareTo(d2.getDate()))
				.filter(lecture -> lecture.getDate().getMonth().equals(date))
				.filter(lecture -> lecture.getGroups().contains(student.getGroup())).collect(Collectors.toList());
	}

	public List<Lecture> getTTForMonth(Teacher teacher, Month date) {
		return lectureService.findAll().stream().sorted((d1, d2) -> d1.getDate().compareTo(d2.getDate()))
				.filter(lecture -> (lecture.getTeacher().equals(teacher) && lecture.getDate().getMonth().equals(date)))
				.collect(Collectors.toList());
	}

	public Gender genderMaker() {
		Gender gender;
		while (true) {
			try {
				String value = reader.readLine();
				exitCheck(value);
				gender = Gender.valueOf(value);
				break;
			} catch (IllegalArgumentException | IOException e) {
				System.out.println("Invalid selection. Please try again");
			}
		}
		return gender;
	}

	public Degree degreeMaker() {
		Degree degree;
		while (true) {
			try {
				String value = reader.readLine();
				exitCheck(value);
				degree = Degree.valueOf(value);
				break;
			} catch (IllegalArgumentException | IOException e) {
				System.out.println("Invalid selection. Please try again");
			}
		}
		return degree;
	}
}

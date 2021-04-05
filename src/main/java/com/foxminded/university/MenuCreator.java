package com.foxminded.university;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Subject;

public class MenuCreator {

	private boolean exit;
	private BufferedReader reader;
	private Cathedra cathedra;
	
	public MenuCreator(Cathedra cathedra) {
		this.cathedra = cathedra;
	}

	private String printMainMenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("=================");
		menu.append(System.lineSeparator());
		menu.append("Chose what you want to do:");
		menu.append(System.lineSeparator());
		menu.append("1 - Create");
		menu.append(System.lineSeparator());
		menu.append("2 - Get info");
		menu.append(System.lineSeparator());
		menu.append("3 - Update");
		menu.append(System.lineSeparator());
		menu.append("4 - Delete");
		menu.append(System.lineSeparator());
		menu.append("0 - Exit");

		return menu.toString();
	}

	private String printCreateMenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("Chose what you want to create:");
		menu.append(System.lineSeparator());
		menu.append("1 - Create new student");
		menu.append(System.lineSeparator());
		menu.append("2 - Create new teacher");
		menu.append(System.lineSeparator());
		menu.append("3 - Create new subject");
		menu.append(System.lineSeparator());
		menu.append("4 - Create new student group");
		menu.append(System.lineSeparator());
		menu.append("5 - Create new lecture");
		menu.append(System.lineSeparator());
		menu.append("6 - Create new holiday");
		menu.append(System.lineSeparator());
		menu.append("0 - Go to the main menu");

		return menu.toString();
	}

	private String printReadMenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("Chose what you want to get:");
		menu.append(System.lineSeparator());
		menu.append("1 - Get student list");
		menu.append(System.lineSeparator());
		menu.append("2 - Get teacher list");
		menu.append(System.lineSeparator());
		menu.append("3 - Get subject list");
		menu.append(System.lineSeparator());
		menu.append("4 - Get group list");
		menu.append(System.lineSeparator());
		menu.append("5 - Get holiday list");
		menu.append(System.lineSeparator());
		menu.append("6 - Get timetable");
		menu.append(System.lineSeparator());
		menu.append("0 - Go to the main menu");

		return menu.toString();
	}

	private String printUpdateMenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("Chose what you want to update:");
		menu.append(System.lineSeparator());
		menu.append("1 - Set student to group");
		menu.append(System.lineSeparator());
		menu.append("2 - Set teacher vacation");
		menu.append(System.lineSeparator());
		menu.append("3 - Set subject to teacher");
		menu.append(System.lineSeparator());
		menu.append("4 - Change lecture audience");
		menu.append(System.lineSeparator());
		menu.append("5 - Change lecture date");
		menu.append(System.lineSeparator());
		menu.append("6 - Change lecture time");
		menu.append(System.lineSeparator());
		menu.append("7 - Set groups to lectures");
		menu.append(System.lineSeparator());
		menu.append("0 - Go to the main menu");

		return menu.toString();
	}

	private String printDeleteMenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("Chose what you want to delete:");
		menu.append(System.lineSeparator());
		menu.append("1 - Delete student");
		menu.append(System.lineSeparator());
		menu.append("2 - Delete teacher");
		menu.append(System.lineSeparator());
		menu.append("3 - Delete group");
		menu.append(System.lineSeparator());
		menu.append("4 - Delete lecture");
		menu.append(System.lineSeparator());
		menu.append("5 - Delete audience");
		menu.append(System.lineSeparator());
		menu.append("6 - Delete subject");
		menu.append(System.lineSeparator());
		menu.append("7 - Delete teacher vacation");
		menu.append(System.lineSeparator());
		menu.append("8 - Delete holiday");
		menu.append(System.lineSeparator());
		menu.append("0 - Go to the main menu");

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
		Formatter formatter = new Formatter();
		DataUpdater du = new DataUpdater();
		int choise = getInput(6);
		switch (choise) {
		case 1:
			System.out.println("Enter student first name:");
			String firstName1 = reader.readLine();
			System.out.println("Enter student last name:");
			String lastName1 = reader.readLine();
			System.out.println("Enter student phone:");
			String phone1 = reader.readLine();
			System.out.println("Enter student address:");
			String address1 = reader.readLine();
			System.out.println("Enter student e-mail:");
			String email1 = reader.readLine();
			System.out.println("Enter student gender:");
			String gender1 = reader.readLine();
			System.out.println("Enter student postal code:");
			String postalCode1 = reader.readLine();
			System.out.println("Enter student education level:");
			String education1 = reader.readLine();
			System.out.println("Enter the date of birth separated by commas without spaces (YEAR,MONTH,DAY):");
			String birthDateString1 = reader.readLine();
			String[] birthArr1=birthDateString1.split(",");
			int birthYear1 = Integer.parseInt(birthArr1[0]);
			int birthMonth1 = Integer.parseInt(birthArr1[1]);
			int birthDay1 = Integer.parseInt(birthArr1[2]);
			LocalDate birthDate1 = LocalDate.of(birthYear1, birthMonth1, birthDay1);
			System.out.println("Set group from list:");
			System.out.println(formatter.formatGroupList(cathedra.getGroups()));
			int groupNumber1 = getInput(cathedra.getGroups().size());
			Group group1 = cathedra.getGroups().get(groupNumber1-1);
			du.createStudent(firstName1, lastName1, phone1, address1, email1, gender1, postalCode1, education1, birthDate1, group1);
			System.out.println("Student added!");
			break;
		case 2:
			System.out.println("Enter teacher first name:");
			String firstName2 = reader.readLine();
			System.out.println("Enter teacher last name:");
			String lastName2 = reader.readLine();
			System.out.println("Enter teacher phone:");
			String phone2 = reader.readLine();
			System.out.println("Enter teacher address:");
			String address2 = reader.readLine();
			System.out.println("Enter teacher e-mail:");
			String email2 = reader.readLine();
			System.out.println("Enter teacher gender:");
			String gender2 = reader.readLine();
			System.out.println("Enter teacher postal code:");
			String postalCode2 = reader.readLine();
			System.out.println("Enter teacher education level:");
			String education2 = reader.readLine();
			System.out.println("Enter the date of birth separated by commas without spaces (YEAR,MONTH,DAY):");
			String birthDateString2 = reader.readLine();
			String[] birthArr2=birthDateString2.split(",");
			int birthYear2 = Integer.parseInt(birthArr2[0]);
			int birthMonth2 = Integer.parseInt(birthArr2[1]);
			int birthDay2 = Integer.parseInt(birthArr2[2]);
			LocalDate birthDate2 = LocalDate.of(birthYear2, birthMonth2, birthDay2);
			System.out.println("Set subject from list:");
			System.out.println(formatter.formatSubjectList(cathedra.getSubjects()));
			String[] subjectsArr2= reader.readLine().split(",");
			List<Subject> subjects2 = new ArrayList<>();
			for(String subjNum : subjectsArr2) {
				Subject subject2 = cathedra.getSubjects().get(Integer.parseInt(subjNum)-1);
				subjects2.add(subject2);
			}
			du.createTeacher(firstName2, lastName2, phone2, address2, email2, gender2, postalCode2, education2, birthDate2, cathedra, subjects2);
			System.out.println("Teacher added!");
			break;
		case 3:
			System.out.println("Create new subject");
			break;
		case 4:
			System.out.println("Create new student group");
			break;
		case 5:
			System.out.println("Create new lecture");
			break;
		case 6:
			System.out.println("Create new holiday");
			break;
		case 0:
			break;
		}
	}

	private void submenuRead() {
		System.out.println(printReadMenu());
		int choise = getInput(6);
		switch (choise) {
		case 1:
			System.out.println("Get student list");
			break;
		case 2:
			System.out.println("Get teacher list");
			break;
		case 3:
			System.out.println("Get subject list");
			break;
		case 4:
			System.out.println("Get group list");
			break;
		case 5:
			System.out.println("Get holiday list");
			break;
		case 6:
			System.out.println("Get timetable");
			break;
		case 0:
			break;
		}
	}

	private void submenuUpdate() {
		System.out.println(printUpdateMenu());
		int choise = getInput(7);
		switch (choise) {
		case 1:
			System.out.println("Set student to group");
			break;
		case 2:
			System.out.println("Set teacher vacation");
			break;
		case 3:
			System.out.println("Set subject to teacher");
			break;
		case 4:
			System.out.println("Change lecture audience");
			break;
		case 5:
			System.out.println("Change lecture date");
			break;
		case 6:
			System.out.println("Change lecture time");
			break;
		case 7:
			System.out.println("Set groups to lectures");
			break;
		case 0:
			break;
		}
	}

	private void submenuDelete() {
		System.out.println(printDeleteMenu());
		int choise = getInput(8);
		switch (choise) {
		case 1:
			System.out.println("Delete student");
			break;
		case 2:
			System.out.println("Delete teacher");
			break;
		case 3:
			System.out.println("Delete group");
			break;
		case 4:
			System.out.println("Delete lecture");
			break;
		case 5:
			System.out.println("Delete audience");
			break;
		case 6:
			System.out.println("Delete subject");
			break;
		case 7:
			System.out.println("Delete teacher vacation");
			break;
		case 8:
			System.out.println("Delete holiday");
			break;
		case 0:
			break;
		}
	}
}

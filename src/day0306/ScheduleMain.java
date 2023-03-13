package day0306;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduleMain {
	public static Scanner sc = new Scanner(System.in);
	public static final int PRINTYEAR = 1, CALSEARCH = 2, DDAY =3 ,INPUT = 4, PRINT = 5, SEARCH = 6, UPDATE = 7, SORT = 8,
			DELETE = 9, EXIT = 10;

	public static void main(String[] args) {
		// 지역변수선언
		boolean run = true;
		int no = 0;
		ArrayList<Schedule> list = new ArrayList<>();
		// 달력 출력하기
//		scheduleCalender(2023,4);
		while (run) {
			no = menuTitle(no);
			switch (no) {
			case PRINTYEAR:
				//2023년도 1월~12월 연간 달력 출력하기
				yearcalendar();
				break;
			case CALSEARCH:
				calendarSearch();
				break;
			case DDAY :	
				ddayprint();
				break;
			case INPUT:
				// 기본정보 입력받기(일정,날짜,진행상황,중요도,메모)
				inputDataSchedule();
				break;
			case PRINT:
				// 전체 정보를 출력하기
				scheduleprintData();
				break;
			case SEARCH:
				// 일정으로 검색하고, 해당 일정을 출력하기
				scheduleTodoSearch();
				break;
			case UPDATE:
				// 수정하기 (날짜, 진행상황, 메모)
				scheduleUpDate();
				break;
			case SORT:
				// 최근날짜 순서로 출력하기
				scheduleSort();
				break;
			case DELETE:
				// 데이터를 삭제하기
				scheduledelete();
				break;
			case EXIT:
				// 종료
				run = false;
				break;
			default:
				System.err.println(" ▶ 숫자 1 ~ 10번까지 입력 가능 : ");
				break;
			} // end of switch
		} // end of while
		System.out.println(" The End");
	}
	// end of main

	private static void ddayprint() {
		Calendar cal = Calendar.getInstance(); //캘린더 불러오기
		int thisYear = cal.get(Calendar.YEAR); // 현재 년 
		int thisMonth = cal.get(Calendar.MONTH) + 1; // 현재 달
		int today = cal.get(Calendar.DATE); // 오늘 일자 저장
		System.out.println();
		System.out.println(" < D-day 계산기 >");
		System.out.print(" 기준일은 " + thisYear + "-" + thisMonth + "-" + today + " 오늘입니다.\n 목표날짜를 입력하세요 : ");
		String targetDay = sc.nextLine();
		
		try {
			// 목표 날짜
			SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
			Date targetDate = yyyyMMdd.parse(targetDay);

			// 현재 날짜
			Date todayDate = new Date();
			String todayDay = yyyyMMdd.format(todayDate);

			long gap = targetDate.getTime() - todayDate.getTime();
			System.out.println(" 목표일까지 "+ (gap / (24 * 60 * 60 * 1000) + 1) + "일 남았습니다 (" + todayDay + " ~ " +  targetDay + ")");
			
		} catch (Exception e) {
			System.out.println("ddayprint 오류" + e.getMessage());
		}
	}
	// 삭제하기
	private static void scheduledelete() {
		int deleteid = inputId();
		DBConnection dbcon = new DBConnection();
		int deleteReturnValue = dbcon.delete(deleteid);
		if (deleteReturnValue == 1) {
			System.out.println(" 삭제 성공");
		} else {
			System.err.println(" 삭제 실패! 아이디를 다시 확인해주세요");
		}
	}

	// 정렬하기(날짜순)
	private static void scheduleSort() {
		int no = 0;
		boolean run = true;
		while (run) {
			System.out.println();
			System.out.println(" ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯ [ 정렬 카테고리 ] ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯ ");
			System.out.println("                       1.ID 오름차순  2.날짜 최근순  3. 종료하기                   ");
			System.out.println(" ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯ ");
			System.out.print(" 정렬사항을 선택해주세요 : ");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case 1: {
				scheduleIDSort();
				break;
			}
			case 2: {
				scheduleDateSort();
				break;
			}
			case 3:
				System.out.println(" 종료합니다.");
				run = false;
				break;
			default:
				System.err.println(" 숫자 1 ~ 3번까지 다시 입력바랍니다 : ");
				break;
			}
		}
	}
	
	private static void updateSortLastTitle() {
		System.out.println(" 수정이 완료되었습니다. \n" + " 추가로 진행하신다면 번호를 선택해주세요.");
	}

	private static void scheduleDateSort() {
		DBConnection dbcon = new DBConnection();
		ArrayList<Schedule> list = dbcon.selectDateSort();
		if (list == null) {
			System.err.println(" 정렬오류");
		} else {
			printPersonSort(list);
		}
	}

	private static void scheduleIDSort() {
		DBConnection dbcon = new DBConnection();
		ArrayList<Schedule> list = dbcon.selectIDSort();
		if (list == null) {
			System.err.println(" 정렬오류");
		} else {
			printPersonSort(list);
		}
	}

	// 일정으로 검색하기
	private static void scheduleTodoSearch() {
		String datatodo = matchingTodoPattern();
		DBConnection dbcon = new DBConnection();
		ArrayList<Schedule> list = dbcon.nameSearchSelect(datatodo);
		if (list.size() >= 1) {
			printSchedule(list);
		} else {
			System.err.println(" 관련된 일정이 없습니다!");
		}

	}

	// 정보출력하기
	private static void scheduleprintData() {
		DBConnection dbcon = new DBConnection();
		ArrayList<Schedule> list = dbcon.select();
		if (list == null) {
			System.err.println(" 선택 실패");
		} else {
			printSchedule(list);
		}
	}

	// 아이디 입력
	private static int inputId() {
		boolean run = true;
		int id = 0;
		while (run) {
			try {
				System.out.println();
				System.out.print(" ID 입력(number) : ");
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (NumberFormatException e) {
				System.err.println(" ID 오류");
			}
		}
		return id;
	}

	// 정렬 toString
	private static void printPersonSort(ArrayList<Schedule> list) {
		System.out.println();
		titleMenu();
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
	}

	// 업데이트
	private static void scheduleUpDate() {
		DBConnection dbcon = new DBConnection();
		int updateReturnValue = 0;
		int id = inputId();
		Schedule csh = dbcon.SclectId(id);
		if (csh == null) {
			System.err.println(" 없는 ID입니다.");
		} else {
			Schedule updataSchedule = updateSchedule(csh);
			updateReturnValue = dbcon.update(updataSchedule);
		}

		if (updateReturnValue == 1) {
			System.out.println(" 수정을 성공적으로 마쳤습니다.");
		} else {
			System.err.println(" 업데이트에 실패하였습니다.");
		}
	}

	// 업데이트 Set
	private static Schedule updateSchedule(Schedule schedule) {
		int no = 0;
		boolean run = true;
		while (run) {
			System.out.println();
			System.out.println(" ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯ [ 수정사항 카테고리 ] ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
			System.out.println("           1.일정  2.날짜  3.진행사항  4.중요도  5.메모  6.수정정보 출력  7.종료        ");
			System.out.println(" ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
			System.out.print(" 수정사항을 선택해주세요 : ");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case 1: {
				String todo = inputTodoSubject("일정", schedule.getTodo());
				schedule.setTodo(todo);
				System.out.println();
				updateSortLastTitle();
				break;
			}
			case 2: {
				String date = inputDateSubject("날짜", schedule.getDate());
				schedule.setDate(date);
				System.out.println();
				updateSortLastTitle();
				break;
			}
			case 3: {
				String action = inputActionSubject("상황", schedule.getAction());
				schedule.setAction(action);
				System.out.println();
				updateSortLastTitle();
				break;
			}
			case 4: {
				String important = inputImportantSubject("중요도", schedule.getImportant());
				schedule.setImportant(important);
				System.out.println();
				updateSortLastTitle();
				break;
			}
			case 5: {
				String memo = inputMemoSubject("메모사항", schedule.getMemo());
				schedule.setMemo(memo);
				System.out.println();
				updateSortLastTitle();
				break;
			}
			case 6 : 
				System.out.println();
				titleMenu();
				System.out.println(schedule);
				break;
			case 7:
				System.out.println(" 종료합니다.");
				run = false;
				break;
			default:
				System.err.println(" 숫자 1 ~ 6번까지 다시 입력바랍니다 : ");
				break;
			}
		}
		return schedule;
	}

	// 업데이트 메모
	private static String inputMemoSubject(String memo, String updataMemo) {
		String memoName = null;
		System.out.println(" 현재 등록되어있는 " + memo + "은 " + "[ " + updataMemo + " ] 입니다. ");
		System.out.print(" 수정사항을 입력하세요(15자이내) : ");
		memoName = memoPattern(memo);
		return memoName;
	}
	
	//업데이트 중요도
	private static String inputImportantSubject(String important, String updataImportant) {
		String importantName = null;
		System.out.println(" 현재 등록되어있는 " + important + "은 " + "[ " + updataImportant + " ] 입니다. ");
		System.out.print(" 수정사항을 입력하세요(상,중,하) : ");
		importantName = importantPattern(important);
		return importantName;
	}

	// 업데이트 진행상황
	private static String inputActionSubject(String action, String updataAction) {
		String actionName = null;
		System.out.println(" 현재 등록되어있는 " + action + "은 " + "[ " + updataAction + " ] 입니다. ");
		System.out.print(" 수정사항을 입력하세요(완료,보류,진행) : ");
		actionName = actionPattern(action);
		return actionName;
	}

	// 업데이트 날짜
	private static String inputDateSubject(String date, String updataDate) {
		String dateName = null;
		System.out.println(" 현재 등록되어있는 " + date + "은 " + "[ " + updataDate + " ] 입니다. ");
		System.out.print(" 수정사항을 입력하세요(yyyy-mm-dd) : ");
		dateName = datePattern(date);
		return dateName;
	}

	//업데이트 일정
	private static String inputTodoSubject(String todo, String updataTodo) {
		String todoName = null;
		System.out.println(" 현재 등록되어있는 " + todo + "은 " + "[ " + updataTodo + " ] 입니다. ");
		System.out.print(" 수정사항을 입력하세요(8자 이내) : ");
		todoName = todoPattern(todo);
		return todoName;
	}

	// 메모 패턴
	private static String memoPattern(String memo) {
		boolean run = true;
		while (run) {
			try {
				memo = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣\\s]{1,15}+$");
				Matcher matcher = pattern.matcher(memo);
				if (!matcher.find()) {
					System.err.print(" 입력오류! 다시 입력하세요(15자 이내) : ");
				} else {
					break;
				}
			} catch (Exception e) {
				System.err.println(" 오류가 발생했습니다.");
				break;
			}
		}
		return memo;
	}

	// 중요도 패턴
	private static String importantPattern(String important) {
		boolean run = true;
		while (run) {
			try {
				important = sc.nextLine();
				if (important.equals("상") || important.equals("중") || important.equals("하")) {
					break;
				} else {
					System.err.print(" 입력오류! 다시 입력하세요(상,중,하) : ");
				}
			} catch (Exception e) {
				System.err.println(" 오류가 발생했습니다.");
				break;
			}
		}
		return important;
	}

	// 진행상황 패턴
	private static String actionPattern(String action) {
		boolean run = true;
		while (run) {
			try {
				action = sc.nextLine();
				if (action.equals("완료") || action.equals("보류") || action.equals("진행")) {
					break;
				} else {
					System.err.print(" 입력오류! 다시 입력하세요(완료,보류,진행) : ");
				}
			} catch (Exception e) {
				System.err.println(" 오류가 발생했습니다.");
				break;
			}
		}
		return action;
	}

	// 날짜 패턴
	private static String datePattern(String date) {
		boolean run = true;
		while (run) {
			try {
				date = sc.nextLine();
				Pattern pattern = Pattern.compile("(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])");
				Matcher matcher = pattern.matcher(date);
				if (!matcher.find()) {
					System.err.print(" 입력오류! yyyy-mm-dd 형식으로 입력하세요 : ");
				} else {
					break;
				}
			} catch (Exception e) {
				System.err.println(" 오류가 발생했습니다.");
				break;
			}
		}
		return date;
	}

	// 일정 패턴
	private static String todoPattern(String todo) {
		boolean run = true;
		while (run) {
			try {
				todo = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣\\s]{1,8}+$");
				Matcher matcher = pattern.matcher(todo);
				if (!matcher.find()) {
					System.err.print(" 입력오류! 다시 입력하세요(8자 이내) : ");
				} else {
					break;
				}
			} catch (Exception e) {
				System.err.println(" 오류가 발생했습니다.");
				break;
			}
		}
		return todo;
	}

	// 할일로 검색하고 패턴적용
	private static String matchingTodoPattern() {
		String todo = null;
		System.out.print(" ▶ 할일을 검색하세요 : ");
		todo = todoPattern(todo);
		return todo;
	}

	// 타이틀 출력하기
	private static void titleMenu() {
		System.out.println("⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯ [ 리스트 출력 ] ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
		System.out.println(
				" ID" + "\t" + "   일정   " + "\t" + "  마감날짜  " + "\t" + "상태" + "\t" + "중요도" + "\t" + "      메모       ");
	}

	// 전체 정보 출력하기
	private static void printSchedule(ArrayList<Schedule> list) {
		System.out.println();
		titleMenu();
		for (Schedule data : list) {
			System.out.println(data);
		}
	}

	// 스캐너로 데이터 입력받기(패턴적용)
	private static Schedule inputDataSchedule() {
		String todo = null, date = null, action = null, important = null, memo = null;
		Calendar cal = Calendar.getInstance(); //캘린더 불러오기
		int thisYear = cal.get(Calendar.YEAR); // 현재 년 
		int thisMonth = cal.get(Calendar.MONTH) + 1; // 현재 달
		
		System.out.println(" ▶ 이번달 달력을 출력합니다.");
		System.out.println();
		getMonthGalendar(thisYear,thisMonth);
		System.out.println(" 데이터를 입력 받겠습니다(1-5)");
		// 일정 입력받기
		System.out.print(" 1.일정입력(최대 8자)     : ");
		todo = todoPattern(todo);
		// 날짜 입력받기
		System.out.print(" 2.날짜입력(yyyy-mm-dd) : ");
		date = datePattern(date);
		// 진행상황 입력받기
		System.out.print(" 3.진행상황(완료,진행,보류) : ");
		action = actionPattern(action);
		// 중요도 입력받기
		System.out.print(" 4.중요도(상,중,하)      : ");
		important = importantPattern(important);
		// 메모 입력받기
		System.out.print(" 5.기타사항(최대 15자)    : ");
		memo = memoPattern(memo);
		// 입력성공 후 저장하기
		Schedule schedule = new Schedule(todo, date, action, important, memo);
		// 데이터베이스 연결
		DBConnection dbcon = new DBConnection();
		int rValue = dbcon.insert(schedule);
		if (rValue == 1) {
			System.out.println(" 삽입에 성공하였습니다.");
		} else {
			System.err.println(" 삽입에 실패하였습니다.");
		}
		return schedule;
	}

	// 번호 선택하기
	private static int menuTitle(int no) {
		System.out.println();
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ [ 일정관리 프로그램 ] ━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println("          1.2023연간달력 2.달력검색 3.D-day 4.일정등록 5.전체출력 6.일정 검색하기         ");
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("           7.수정하기 8.정렬(ID 오름차순, 최근날짜) 9.데이터 삭제하기 10.종료하기           ");
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.print(" ▶ 원하는 숫자(1-10)를 입력하세요: ");
		no = Integer.parseInt(sc.nextLine());
		return no;
	}
	
	//패턴(년도)
	private static int calendarSearchYearSearch() {
		int inputYear = 0;
		boolean run = true;
		while (run) {
			try {
				inputYear = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{4}$");
				Matcher matcher = pattern.matcher(String.valueOf(inputYear));
				if (!matcher.find()) {
					System.err.print(" 입력오류! 다시 입력하세요(yyyy) : ");
				} else {
					break;
				}
			} catch (Exception e) {
				System.err.println(" 오류가 발생했습니다.");
				break;
			}
		}
		return inputYear;
	}
	
//	//패턴(월)
//	private static int calendarSearchMonthSearch() {
//		int inputMonth = 0;
//		boolean run = true;
//		//"^0[1-9]|1[0-2]{2}$"
//		while (run) {
//			try {
//				inputMonth = Integer.parseInt(sc.nextLine());
//				Pattern pattern = Pattern.compile("^(0[1-9]|1[012])$");
//				Matcher matcher = pattern.matcher(String.valueOf(inputMonth));
//				if (  !matcher.find()) {
//					System.err.print(" 입력오류! 다시 입력하세요(mm) : ");
//				} else {
//					break;
//				}
//			} catch (Exception e) {
//				System.err.println(" 오류가 발생했습니다.");
//				break;
//			}
//		}
//		return inputMonth;
//	}
	
	// 원하는 달력 출력하기
	private static void calendarSearch() {
		int inputYear = 0, inputMonth = 0;
		System.out.println("\n < 검색할 정보를 입력하세요 >");
		System.out.print(" ▶ 년도(yyyy) 입력 : ");
		inputYear = calendarSearchYearSearch();
		System.out.print(" ▶ 월(mm) 입력 : ");
		inputMonth = Integer.parseInt(sc.nextLine());
		//inputMonth = calendarSearchMonthSearch();
		System.out.println();
		ScheduleMain.getMonthGalendar(inputYear, inputMonth);
	}

	// 연간 달력출력
	private static void yearcalendar() {
		System.out.println(" ▶ 2023년도 연간 달력을 출력합니다.");
		//1월 - 12월 숫자 for문으로 돌리기
		for (int i = 1; i < 13; i++) {
			System.out.println();
			ScheduleMain.getMonthGalendar(2023, i);
		}
		System.out.println(" ▶ 1월 ~ 12월 달력입니다. ");
	}

	// 달력 출력하기
	public static void getMonthGalendar(int intYear, int intMmonth) {
		Calendar cal = Calendar.getInstance(); //캘린더 불러오기
		int thisYear = cal.get(Calendar.YEAR); // 현재 년 
		int thisMonth = cal.get(Calendar.MONTH) + 1; // 현재 달
		int today = cal.get(Calendar.DATE); // 오늘 일자 저장
		boolean booToday = (intYear == thisYear) && (thisMonth == intMmonth);
		cal.set(intYear, intMmonth - 1, 1); // 캘린더객체에 입력받은 년, 달, 그리고 Date을 1로설정
		int sDayNum = cal.get(Calendar.DAY_OF_WEEK); // 1일의 요일 얻어오기
		int endDate = cal.getActualMaximum(Calendar.DATE); // 달의 마지막일 얻기
		int nowYear = cal.get(Calendar.YEAR); // 연도 얻기
		int nowMonth = cal.get(Calendar.MONTH); // 월 얻기
		System.out.println("   \t    [ " + nowYear + "년 " + (nowMonth + 1) + "월 ] ");
		System.out.println();
		System.out.println("  Sun  Mon  Tue  Wed  Thu  Fri  Sat ");
		// 1일이 시작되는 이전의 요일 공백으로 채우기
		int intDateNum = 1; // 출력할 date를 저장할 변수
		for (int i = 1; intDateNum <= endDate; i++) { // 출력한 Date 변수(intDateNum)가 1일부터 마지막일이 될때까지 반복.
			if (i < sDayNum)
				System.out.printf(" %4s", ""); // i가 요일숫자보다 작으면 공백으로 채우기
			else {
				if (booToday && intDateNum == today)
					System.out.printf(" [%1d] ", intDateNum); // 오늘 날짜 표시
				else
					System.out.printf(" %3d ", intDateNum); // 일반 출력
				intDateNum++; // 출력할 date 증가.
			}
			if (i % 7 == 0)
				System.out.println(); // i가 7의배수가 되면 줄바꿈.

		}
		System.out.println("\n⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
	}
}
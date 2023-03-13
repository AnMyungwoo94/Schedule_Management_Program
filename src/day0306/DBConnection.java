package day0306;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class DBConnection {
	// 멤버변수
	private Connection connection = null;

	// 생성자 : 디폴트생성자
	// 멤버함수
	public void connect() {
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("C:/202301JAVAWorkspace/Scheduleprogram/src/day0306/db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e ) {
			System.out.println("[데이타베이스 로드오류]" + e.getStackTrace());
		} catch (IOException e) {
			System.out.println("[데이타베이스 연결오류]" + e.getStackTrace());
		}
	
	// 2.내부적으로 JDBC 드라이버매니저를 통해서 DB와 연결을 가져온다.
	try {
		// 1. jdbc 클래스로드
		Class.forName(properties.getProperty("driverName"));
		// 2. mysql DB 연결
		connection = DriverManager.getConnection(properties.getProperty("url"), 
				properties.getProperty("user"),
				properties.getProperty("password"));
	} catch (ClassNotFoundException e) {
		System.out.println("[데이타베이스 로드오류]" + e.getStackTrace());
	} catch (SQLException e) {
		System.out.println("[데이타베이스 연결오류]" + e.getStackTrace());
	}
}
	// 1. 데이터삽입
	public int insert(Schedule s) {
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "insert into scheduleTBL values(null, ?, ?, ?, ?, ?)";
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, s.getTodo());
			ps.setString(2, s.getDate());
			ps.setString(3, s.getAction());
			ps.setString(4, s.getImportant());
			ps.setString(5, s.getMemo());

			// 삽입 성공하면 1을 리턴한다.
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return returnValue;
	}

	// 선택 statement
	public ArrayList<Schedule> select() {
		ArrayList<Schedule> list = new ArrayList<>();

		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from scheduleTBL";

		try {
			ps = connection.prepareStatement(query);
			// Select 성공하면 ResultSet / 실패하면 null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String todo = rs.getString("todo");
				String date = rs.getString("date");
				String action = rs.getString("action");
				String important = rs.getString("important");
				String memo = rs.getString("memo");
				list.add(new Schedule(id, todo, date, action, important, memo));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return list;
	}

	// 일정 검색
	public ArrayList<Schedule> nameSearchSelect(String datatodo) {
		ArrayList<Schedule> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from scheduleTBL where todo like ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, "%" + datatodo + "%");
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String todo = rs.getString("todo");
				String date = rs.getString("date");
				String action = rs.getString("action");
				String important = rs.getString("important");
				String memo = rs.getString("memo");
				list.add(new Schedule(id, todo, date, action, important, memo));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return list;
	}

	// 셀렉트 아이디
	public Schedule SclectId(int dataid) {
		Schedule schedule = null;
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from scheduleTBL where id = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, dataid);
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			if (rs.next()) {
				int id = rs.getInt("id");
				String todo = rs.getString("todo");
				String date = rs.getString("date");
				String action = rs.getString("action");
				String important = rs.getString("important");
				String memo = rs.getString("memo");
				schedule = new Schedule(id, todo, date, action, important, memo);
			}
		} catch (Exception e) {
			System.out.println("select id 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return schedule;
	}

	// 업데이트
	public int update(Schedule s) {
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "update scheduleTBL  set  date =?, action = ?,  memo = ? where id= ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, s.getDate());
			ps.setString(2, s.getAction());
			ps.setString(3, s.getMemo());
			ps.setInt(4, s.getId());
			// 삽입 성공하면 1을 리턴한다.엔터
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("update 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return returnValue;
	}
	
	//날짜순으로 정렬
	public ArrayList<Schedule> selectDateSort() {
		ArrayList<Schedule> list = new ArrayList<>();

		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from scheduleTBL order by date asc";

		try {
			ps = connection.prepareStatement(query);
			// Select 성공하면 ResultSet / 실패하면 null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String todo = rs.getString("todo");
				String date = rs.getString("date");
				String action = rs.getString("action");
				String important = rs.getString("important");
				String memo = rs.getString("memo");
				list.add(new Schedule(id, todo, date, action, important, memo));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return list;
	}

	public ArrayList<Schedule> selectIDSort() {
		ArrayList<Schedule> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from scheduleTBL order by id desc";

		try {
			ps = connection.prepareStatement(query);
			// Select 성공하면 ResultSet / 실패하면 null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String todo = rs.getString("todo");
				String date = rs.getString("date");
				String action = rs.getString("action");
				String important = rs.getString("important");
				String memo = rs.getString("memo");
				list.add(new Schedule(id, todo, date, action, important, memo));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return list;
	}

	public int delete(int deleteid) {
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "delete  from scheduleTBL where id  = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, deleteid);
			// 삽입 성공하면 1을 리턴한다.엔터
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert 오류발생 " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
		return returnValue;
	}
}

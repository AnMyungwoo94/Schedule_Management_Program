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
	// �������
	private Connection connection = null;

	// ������ : ����Ʈ������
	// ����Լ�
	public void connect() {
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("C:/202301JAVAWorkspace/Scheduleprogram/src/day0306/db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e ) {
			System.out.println("[����Ÿ���̽� �ε����]" + e.getStackTrace());
		} catch (IOException e) {
			System.out.println("[����Ÿ���̽� �������]" + e.getStackTrace());
		}
	
	// 2.���������� JDBC ����̹��Ŵ����� ���ؼ� DB�� ������ �����´�.
	try {
		// 1. jdbc Ŭ�����ε�
		Class.forName(properties.getProperty("driverName"));
		// 2. mysql DB ����
		connection = DriverManager.getConnection(properties.getProperty("url"), 
				properties.getProperty("user"),
				properties.getProperty("password"));
	} catch (ClassNotFoundException e) {
		System.out.println("[����Ÿ���̽� �ε����]" + e.getStackTrace());
	} catch (SQLException e) {
		System.out.println("[����Ÿ���̽� �������]" + e.getStackTrace());
	}
}
	// 1. �����ͻ���
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

			// ���� �����ϸ� 1�� �����Ѵ�.
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert �����߻� " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		}
		return returnValue;
	}

	// ���� statement
	public ArrayList<Schedule> select() {
		ArrayList<Schedule> list = new ArrayList<>();

		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from scheduleTBL";

		try {
			ps = connection.prepareStatement(query);
			// Select �����ϸ� ResultSet / �����ϸ� null
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
			System.out.println("select �����߻� " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		}
		return list;
	}

	// ���� �˻�
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
			System.out.println("select �����߻� " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		}
		return list;
	}

	// ����Ʈ ���̵�
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
			System.out.println("select id �����߻� " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		}
		return schedule;
	}

	// ������Ʈ
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
			// ���� �����ϸ� 1�� �����Ѵ�.����
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("update �����߻� " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		}
		return returnValue;
	}
	
	//��¥������ ����
	public ArrayList<Schedule> selectDateSort() {
		ArrayList<Schedule> list = new ArrayList<>();

		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from scheduleTBL order by date asc";

		try {
			ps = connection.prepareStatement(query);
			// Select �����ϸ� ResultSet / �����ϸ� null
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
			System.out.println("select �����߻� " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
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
			// Select �����ϸ� ResultSet / �����ϸ� null
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
			System.out.println("select �����߻� " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
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
			// ���� �����ϸ� 1�� �����Ѵ�.����
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert �����߻� " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
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

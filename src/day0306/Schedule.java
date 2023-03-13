package day0306;

import java.util.Objects;

public class Schedule  {
	// ������� ����
	private int id;
	private String todo;
	private String date;
	private String action;
	private String important;
	private String memo;

	// ������ �����ε�
	public Schedule() {
	}
	public Schedule(String todo, String date, String action, String important, String memo) {
		this(0, todo, date, action, important, memo);
	}

	public Schedule(int id, String todo, String date, String action, String important, String memo) {
		super();
		this.id = id;
		this.todo = todo;
		this.date = date;
		this.action = action;
		this.important = important;
		this.memo = memo;
	}

	// get, set����
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTodo() {
		return todo;
	}

	public void setTodo(String todo) {
		this.todo = todo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getImportant() {
		return important;
	}

	public void setImportant(String important) {
		this.important = important;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	// toString ����
	@Override
	public String toString() {
		return " " + id + "\t" + todo + "\t" + date + "\t" + action + " \t " + important + " \t " + memo;
	}

	// �ؽ��ڵ� ����
	@Override
	public int hashCode() {
		return Objects.hash(id, date);
	}

	// equals����
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Schedule) {
			Schedule schedule = (Schedule) obj;
			return (this.id == schedule.id) && (this.todo.equals(schedule.date));
		}
		return false;
	}
}


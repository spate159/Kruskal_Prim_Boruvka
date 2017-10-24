public class Coordinates {
	private float x;
	private float y;
	public Coordinates(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Coordinates scale(float k) {
		return new Coordinates(k * x, k * y);
	}

	public Coordinates add(Coordinates c) {
		return new Coordinates(x + c.x, y + c.y);
	}

	public Coordinates subtract(Coordinates c) {
		return new Coordinates(x - c.x, y - c.y);
	}

	public Coordinates unit() {

			return new Coordinates(x / (float) Math.sqrt(x * x + y * y),
					y / (float) Math.sqrt(y * y + x * x));
	}
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public int hashCode() {
		return ((((Float.hashCode(x) + Float.hashCode(y))
				* (Float.hashCode(x) + Float.hashCode(y) + 1)) / 2)
				+ Float.hashCode(y));
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == this)
			return true;
		if (!(obj instanceof Edge)) {
			return false;
		}
		Coordinates c = (Coordinates) obj;
		return c.getX() == x && c.getY() == y;
	}
}
package org.resistance.graph;

/**
 * 
 * @author qn
 */
public class EdgeIndex {
	int s;
	int t;

	public EdgeIndex(int s, int t) {
		this.s = s;
		this.t = t;
	}

	public int getS() {
		return s;
	}

	public int getT() {
		return t;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + s;
		result = prime * result + t;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgeIndex other = (EdgeIndex) obj;
		if (s != other.s)
			return false;
		if (t != other.t)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Edge [s=" + s + ", t=" + t + "]";
	}
}

/*
 * Copyright 2012 Decebal Suiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
 * the License. You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ro.fortsoft.wicket.dashboard;

import java.io.Serializable;

/**
 * @author Decebal Suiu
 */
public class WidgetLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	private int column;
	private int row;
    private int columnSpan = 1;
    private int rowSpan = 1;

	public WidgetLocation() {
	}

	public WidgetLocation(int column, int row) {
		this.column = column;
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void incrementRow() {
		row++;
	}

	public void decrementRow() {
		row--;
	}

    public int getColumnSpan() {
        return columnSpan;
    }

    public void setColumnSpan(int columnSpan) {
        this.columnSpan = columnSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
        result = prime * result + columnSpan;
        result = prime * result + rowSpan;

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		WidgetLocation other = (WidgetLocation) obj;
		if (column != other.column) {
			return false;
		}
		if (row != other.row) {
			return false;
		}
        if (rowSpan != other.rowSpan) {
            return false;
        }
        if (columnSpan != other.columnSpan) {
            return false;
        }

		return true;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("WidgetLocation[");
		buffer.append("column = ").append(column);
		buffer.append(" row = ").append(row);
        buffer.append(" columnSpan = ").append(columnSpan);
        buffer.append(" rowSpan = ").append(rowSpan);
		buffer.append("]");

		return buffer.toString();
	}

}

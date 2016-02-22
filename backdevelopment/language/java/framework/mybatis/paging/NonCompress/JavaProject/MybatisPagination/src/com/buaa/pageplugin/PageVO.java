package com.buaa.pageplugin;
/** 
 * @ProjectName BooksSystem
 * @PackageName com.book.vo
 * @ClassName PageVO
 * @Description 分页VO
 * @Author 刘吉超
 * @Date 2015-10-29 16:09:31
 */
public class PageVO {
	// 当前页
	private int currentPage = 1;
	
	// 每页记录数
	private int rowsPerPage = 10;
	
	// 当前页最小记录编号
	private int currentPageMinRow = 0;
	
	// 当前页最大记录编号
	private int currentPageMaxRow = 0;
	
	// 总记录数
	private int totalRowCount = 0;
	
	// 总页数
	private int totalPage = 0;
	// 分页功能是否开启
	private boolean openPage = false;
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public void setCurrentPage(int currentPage) {
		if(currentPage > 0){
			this.currentPage = currentPage;
		}
	}
	
	public int getRowsPerPage() {
		return rowsPerPage;
	}
	
	public void setRowsPerPage(int rowsPerPage) {
		if(rowsPerPage > 0){
			this.rowsPerPage = rowsPerPage;
		}
	}
	
	public int getTotalRowCount() {
		return totalRowCount;
	}
	
	public void setTotalRowCount(int totalRowCount) {
		if(totalRowCount >= 0){
			this.totalRowCount = totalRowCount;
			
			// 计算总页数
			if(this.totalRowCount%this.rowsPerPage == 0){
				this.totalPage = this.totalRowCount/this.rowsPerPage;
			}else{
				this.totalPage = this.totalRowCount/this.rowsPerPage + 1;
			}
		}
	}
	
	public int getTotalPage() {
		return totalPage;
	}
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPageMinRow() {
		// 计算当前页最小记录编号
		this.currentPageMinRow = (this.currentPage - 1) * this.rowsPerPage;
		// 获得当前页最小记录编号
		return this.currentPageMinRow;
	}

	public void setCurrentPageMinRow(int currentPageMinRow) {
		this.currentPageMinRow = currentPageMinRow;
	}

	public int getCurrentPageMaxRow() {
		// 计算当前页最大记录编号
		currentPageMaxRow = this.currentPage * this.rowsPerPage;
		// 获得当前页最大记录编号
		return currentPageMaxRow;
	}

	public void setCurrentPageMaxRow(int currentPageMaxRow) {
		this.currentPageMaxRow = currentPageMaxRow;
	}

	public boolean isOpenPage() {
		return openPage;
	}

	public void setOpenPage(boolean openPage) {
		this.openPage = openPage;
	}
}
package kr.ac.kopo.ctc.kopo21.board;


public class PaginationInfo {
    private int firstPage;
    private int prevPage;
    private int startPage;
    private int currentPage;
    private int endPage;
    private int nextPage;
    private int lastPage;
    private int totalPageCount;

    public int getFirstPage() { return firstPage; }
    public void setFirstPage(int firstPage) { this.firstPage = firstPage; }

    public int getPrevPage() { return prevPage; }
    public void setPrevPage(int prevPage) { this.prevPage = prevPage; }

    public int getStartPage() { return startPage; }
    public void setStartPage(int startPage) { this.startPage = startPage; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getEndPage() { return endPage; }
    public void setEndPage(int endPage) { this.endPage = endPage; }

    public int getNextPage() { return nextPage; }
    public void setNextPage(int nextPage) { this.nextPage = nextPage; }

    public int getLastPage() { return lastPage; }
    public void setLastPage(int lastPage) { this.lastPage = lastPage; }

    public int getTotalPageCount() { return totalPageCount; }
    public void setTotalPageCount(int totalPageCount) { this.totalPageCount = totalPageCount; }

}

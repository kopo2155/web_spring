package kr.ac.kopo.ctc.kopo21.board.repository;

import kr.ac.kopo.ctc.kopo21.board.PaginationInfo;
import kr.ac.kopo.ctc.kopo21.board.service.PaginationInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PaginationInfoServiceTest {

    @Autowired
    PaginationInfoService paginationInfoService;

    void check(PaginationInfo paginationInfo, int firstPage, int prevPage, int startPage, int currentPage, int endPage, int nextPage, int lastPage, int totalPageCount) {
        assertEquals(paginationInfo.getFirstPage(),firstPage);
        assertEquals(paginationInfo.getPrevPage(), prevPage);
        assertEquals(paginationInfo.getStartPage(), startPage);
        assertEquals(paginationInfo.getCurrentPage(),currentPage);
        assertEquals(paginationInfo.getEndPage(), endPage);
        assertEquals(paginationInfo.getNextPage(),nextPage);
        assertEquals(paginationInfo.getLastPage(),lastPage);
        assertEquals(paginationInfo.getTotalPageCount(),totalPageCount);
    }

    @Test
    void getPaginationInfo01() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(1,5,1,1);
        check(paginationInfo, -1, -1, 1,1,-1,-1,-1,1);
    }

    @Test
    void getPaginationInfo02() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(1,5,10,1);
        check(paginationInfo, -1, -1, 1,1,-1,-1,-1,1);
    }
    @Test
    void getPaginationInfo03() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(3,5,10,50);
        check(paginationInfo, -1, -1, 1,3,5,-1,-1,5);
    }
    @Test
    void getPaginationInfo04() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(5,5,10,50);
        check(paginationInfo, -1, -1, 1,5,5,-1,-1,5);
    }
    @Test
    void getPaginationInfo05() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(5,5,10,100);
        check(paginationInfo, -1, -1,  1,5,5,6,10,10);
    }
    @Test
    void getPaginationInfo06() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(6,5,10,100);
        check(paginationInfo, 1, 5, 6,6,10,-1,-1,10);
    }
    @Test
    void getPaginationInfo07() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(9,5,10,95);
        check(paginationInfo, 1, 5, 6,9,10,-1,-1,10);
    }
    @Test
    void getPaginationInfo08() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(10,5,10,95);
        check(paginationInfo, 1, 5, 6,10,10,-1,-1,10);
    }
    @Test
    void getPaginationInfo09() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(10,1,1,10);
        check(paginationInfo, 1, 9, 10,10,10,-1,-1,10);
    }
    @Test
    void getPaginationInfo10() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(1,2,2,7);
        check(paginationInfo, -1, -1, 1,1,2,3,4,4);
    }
    @Test
    void getPaginationInfo11() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(2,2,2,7);
        check(paginationInfo, -1, -1, 1,2,2,3,4,4);
    }
    @Test
    void getPaginationInfo12() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(3,2,2,7);
        check(paginationInfo, 1, 2, 3,3,4,-1,-1,4);
    }
    @Test
    void getPaginationInfo13() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(4,2,2,7);
        check(paginationInfo, 1, 2, 3,4,4,-1,-1,4);
    }

    @Test
    void getPaginationInfo14() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(274,10,50,13674);
        check(paginationInfo, 1, 270, 271,274,274,-1,-1,274);
    }

    @Test
    void getPaginationInfo15() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(275,10,50,13674);
        check(paginationInfo, 1, 270, 271,274,274,-1,-1,274);
    }

    @Test
    void getPaginationInfo16() {
        PaginationInfo paginationInfo = paginationInfoService.getPaginationInfo(-275,10,50,13674);
        check(paginationInfo, -1, -1, 1,1,10,11,274,274);
    }


}


package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AlbumServiceTest {

    @Autowired
    AlbumService albumService;

    @Autowired
    AlbumRepository albumRepository;

    @Test
    void getAlbum() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        AlbumDto resAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
        assertEquals("테스트", resAlbum.getAlbumName());
    }

    @Test
    void testGetAlbum() {
    }

    @Test
    void testAlbumRepository() throws InterruptedException {
        Album album1 = new Album();
        Album album2 = new Album();
        album1.setAlbumName("aaaa");
        album2.setAlbumName("aaab");

        albumRepository.save(album1);
        TimeUnit.SECONDS.sleep(1);
        albumRepository.save(album2);

        // 최신순 정렬, 두번째로 생성한 앨범이 먼저 나와야함
        List<Album> resDate = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc("aaa");
        // 0번째 index가 두번째 앨범명 aaab인지 체크
        assertEquals("aaab", resDate.get(0).getAlbumName());
        // 1번째 index가 첫번째 앨범명 aaab인지 체크
        assertEquals("aaaa", resDate.get(1).getAlbumName());
        // aaa 이름을 가진 다른 앨범이 없다는 가정하에, 검색 키워드에 해당하는 앨범 필터링 체크
        assertEquals(2, resDate.size());

        // 앨범명 정렬, aaaa -> aaab 기준으로 나와야함
        List<Album> resName = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc("aaa");
        // 0번째 index가 두번째 앨범명 aaaa인지 체크
        assertEquals("aaaa", resName.get(0).getAlbumName());
        // 1번째 index가 두번째 앨범명 aaab인지 체크
        assertEquals("aaab", resName.get(1).getAlbumName());
        // aaa 이름을 가진 다른 앨범이 없다는 가정하에, 검색 키워드에 해당하는 앨범 필터링 체크
        assertEquals(2, resName.size());
    }

    @Test
    void testChangeAlbumName() throws IOException {

        // 앨범 생성
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("변경 전");
        AlbumDto res = albumService.createAlbum(albumDto);
        // 생성된 앨범 아이디 추출
        Long albumId = res.getAlbumId();

        AlbumDto updateDto = new AlbumDto();
        // 업데이트용 Dto 생성
        updateDto.setAlbumName("변경 후");
        albumService.changeName(albumId, updateDto);

        AlbumDto updatedDto = albumService.getAlbum(albumId);

        // 앨범명이 변경되었는지 확인
        assertEquals("변경 후", updatedDto.getAlbumName());
    }
}
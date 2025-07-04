// package com.example.cluvrapi.domain.board.controller;
//
// import static org.mockito.BDDMockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// import java.time.LocalDateTime;
// import java.util.List;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
// import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
// import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
// import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
// import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;
//
// import com.example.cluvrapi.domain.board.dto.request.CreateBoardRequestDto;
// import com.example.cluvrapi.domain.board.dto.request.UpdateBoardRequestDto;
// import com.example.cluvrapi.domain.board.dto.response.ReadBoardResponseDto;
// import com.example.cluvrapi.domain.board.dto.response.ReadMyBoardsResponseDto;
// import com.example.cluvrapi.domain.board.enums.BoardType;
// import com.example.cluvrapi.domain.board.service.BoardService;
// import com.example.cluvrapi.domain.category.enums.CategoryType;
// import com.example.cluvrapi.domain.common.dto.AuthUser;
// import com.example.cluvrapi.domain.common.dto.PageResponseDto;
// import com.example.cluvrapi.global.resolver.AuthenticationArgumentResolver;
// import com.fasterxml.jackson.databind.ObjectMapper;
//
// @TestPropertySource(properties = "audit.enabled=false")
// // WebMvcTest: 스프링에서 딱 컨트롤러까지만 구동시켜 줌
// @WebMvcTest(controllers = BoardController.class,
// 	excludeAutoConfiguration = {
// 		//@WebMvcTest가 JPA 관련 빈들을 로드하려고 해서 발생하는 문제가 발생. JPA Auditing이나 Entity 스캔 때문에 생긴 것 같다고 함. 그래서 JPA 자동 설정 제외
// 		SecurityAutoConfiguration.class,
// 		JpaRepositoriesAutoConfiguration.class,
// 		HibernateJpaAutoConfiguration.class,
// 		DataSourceAutoConfiguration.class,
// 		DataSourceTransactionManagerAutoConfiguration.class
// 	})
// class BoardControllerTest {
// 	// controller : DispatcherServlet, RequestHandler, AdapterController 테스트 진행 하는 것
//
// 	@Autowired
// 	private MockMvc mockMvc; // WebMvcTest 사용시, MockMvc 선언 필요
//
// 	@Autowired
// 	private ObjectMapper objectMapper; // JSON 변환용
//
// 	@MockitoBean
// 	BoardService boardService;
//
// 	@MockitoBean
// 	AuthenticationArgumentResolver authenticationArgumentResolver;
//
// 	@MockitoBean
// 	PageableHandlerMethodArgumentResolver pageableResolver;
//
// 	@Test
// 	@DisplayName("보드 생성 테스트")
// 	void createBoard() throws Exception {
// 		// Given: 준비
// 		// 요청: CreateBoardRequestDto
// 		// 반환: Long (board ID)
// 		CreateBoardRequestDto request = new CreateBoardRequestDto(
// 			"title",
// 			"content",
// 			BoardType.CHITCHAT,
// 			CategoryType.OTHERS,
// 			10
// 		);
// 		AuthUser user = new AuthUser(1L, "test@example.com");
// 		Long fakeBoardId = 1L;
//
// 		// BDD 스타일로 모킹 설정
// 		// Mock authentication argument resolver
// 		given(authenticationArgumentResolver.supportsParameter(any())).willReturn(true);
// 		given(authenticationArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(user);
// 		// 만약 boardService.createBoard()가 request와 똑같은 값을 받으면, fakeBoardId(1L)을 리턴해라.
// 		given(boardService.createBoard(eq(user.id()), any(CreateBoardRequestDto.class))).willReturn(fakeBoardId);
//
// 		// When, Then: 호출 및 결과 확인
//
// 		mockMvc.perform(post("/api/boards")
// 				.contentType(MediaType.APPLICATION_JSON) // post 요청에 json 데이터를 담아서 보내려면 contentType과 content 설정 필요
// 				.content(objectMapper.writeValueAsString(request))) // 객체를 json 문자열로 변환
// 			.andExpect(jsonPath("$.result.status").value(201))
// 			.andExpect(jsonPath("$.data").value(fakeBoardId))
// 			.andExpect(jsonPath("$.result.code").value("CREATED"));
//
// 	}
//
// 	@Test
// 	@DisplayName(("카테고리별 게시글 목록 조회 테스트"))
// 	void readBoards() throws Exception {
// 		CategoryType category = CategoryType.OTHERS;
// 		int pageNumber = 0;
// 		int pageSize = 5;
// 		Pageable pageable = PageRequest.of(pageNumber, pageSize);
//
//
// 		List<ReadBoardResponseDto> content = List.of(
// 			new ReadBoardResponseDto(1L, "title1", "content1", 5, "nickname1", LocalDateTime.now(), LocalDateTime.now()),
// 			new ReadBoardResponseDto(2L, "title2", "content2", 50, "nickname2", LocalDateTime.now(), LocalDateTime.now())
// 		);
//
// 		PageResponseDto<ReadBoardResponseDto> fakeResponse =
// 			PageResponseDto.<ReadBoardResponseDto>builder()
// 				.content(content)
// 				.page(1)
// 				.size(5)
// 				.totalElements(2L)
// 				.totalPages(1)
// 				.build();
//
// 		given(pageableResolver.supportsParameter(any())).willReturn(true);
// 		given(pageableResolver.resolveArgument(any(), any(), any(), any()))
// 			.willReturn(PageRequest.of(pageNumber, pageSize));
//
// 		given(boardService.readBoards(category, pageable)).willReturn(fakeResponse);
//
// 		mockMvc.perform(get("/api/boards")
// 				.param("category", category.name())
// 				.param("pageNumber", String.valueOf(pageNumber))
// 				.param("pageSize", String.valueOf(pageSize)))
// 			.andExpect(status().isOk())
// 			.andExpect(jsonPath("$.result.status").value(200))
// 			.andExpect(jsonPath("$.data.content").isArray())
// 			.andExpect(jsonPath("$.data.content.length()").value(content.size()))
// 			.andExpect(jsonPath("$.data.content[0].title").value("title1"))
// 			.andExpect(jsonPath("$.data.content[1].userName").value("nickname2"));
//
// 	}
//
// 	@Test
// 	@DisplayName("게시글 단건 조회")
// 	void readBoard() throws Exception {
// 		long boardId = 1L;
// 		ReadBoardResponseDto response = new ReadBoardResponseDto(
// 			boardId,
// 			"title1",
// 			"content1",
// 			CategoryType.AI_DATA_SCIENCE,
// 			false,
// 			2,
// 			5,
// 			"nickname1",
// 			2,
// 			0,
// 			LocalDateTime.now(),
// 			LocalDateTime.now()
// 		);
//
// 		given(boardService.readBoard(boardId)).willReturn(response);
//
// 		mockMvc.perform(get("/api/boards/{boardId}", boardId))
// 			.andExpect(status().isOk())
// 			.andExpect(jsonPath("$.result.status").value(200))
// 			.andExpect(jsonPath("$.data.id").value(boardId))
// 			.andExpect(jsonPath("$.data.clover").value(2))
// 			.andExpect(jsonPath("$.data.title").value("title1"))
// 			.andExpect(jsonPath("$.data.content").value("content1"));
// 	}
//
// 	@Test
// 	@DisplayName("보드 업데이트 테스트")
// 	void updateBoard() throws Exception {
// 		AuthUser user = new AuthUser(1L, "test@example.com");
// 		UpdateBoardRequestDto request = new UpdateBoardRequestDto(
// 			"updatedTitle",
// 			"updatedContent",
// 			10
// 		);
// 		Long boardId = 1L;
//
// 		// BDD 스타일로 모킹 설정
// 		// Mock authentication argument resolver
// 		given(authenticationArgumentResolver.supportsParameter(any())).willReturn(true);
// 		given(authenticationArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(user);
// 		// 만약 boardService.createBoard()가 request와 똑같은 값을 받으면, fakeBoardId(1L)을 리턴해라.
// 		// boardService.updateBoard()는 void이므로 doNothing() 설정
// 		willDoNothing().given(boardService).updateBoard(
// 			eq(user.id()), any(UpdateBoardRequestDto.class), eq(boardId)
// 		);
//
// 		// when & then
// 		mockMvc.perform(patch("/api/boards/{boardId}", boardId)
// 				.contentType(MediaType.APPLICATION_JSON)
// 				.content(objectMapper.writeValueAsString(request)))
// 			.andExpect(status().isOk())
// 			.andExpect(jsonPath("$.result.status").value(200))
// 			.andExpect(jsonPath("$.result.code").value("OK"));
//
// 	}
//
// 	@Test
// 	@DisplayName("보드 삭제")
// 	void deleteBoard() throws Exception {
// 		AuthUser user = new AuthUser(1L, "test@example.com");
// 		Long boardId = 1L;
//
// 		willDoNothing().given(boardService).deleteBoard(eq(user.id()), eq(boardId));
//
// 		mockMvc.perform(delete("/api/boards/{boardId}", boardId)
// 				.contentType(MediaType.APPLICATION_JSON))
// 			.andExpect(status().isOk())
// 			.andExpect(jsonPath("$.result.status").value(200))
// 			.andExpect(jsonPath("$.result.code").value("OK"));
// 	}
//
// 	@Test
// 	@DisplayName("내가 쓴 게시글 목록 조회")
// 	void readBoardsWithUser() throws Exception {
// 		AuthUser user = new AuthUser(1L, "test@example.com");
//
// 		List<ReadMyBoardsResponseDto> content = List.of(
// 			new ReadMyBoardsResponseDto(1L, "title1", "content1", LocalDateTime.now()),
// 			new ReadMyBoardsResponseDto(2L, "title2", "content2", LocalDateTime.now())
// 		);
//
// 		PageResponseDto<ReadMyBoardsResponseDto> pageResponseDto = PageResponseDto.<ReadMyBoardsResponseDto>builder()
// 			.content(content)
// 			.page(1)
// 			.size(5)
// 			.totalElements(2L)
// 			.totalPages(1)
// 			.build();
// 		// Mock authentication argument resolver
// 		given(authenticationArgumentResolver.supportsParameter(argThat(
// 			p -> p.getParameterType().equals(AuthUser.class)
// 		))).willReturn(true);
// 		given(authenticationArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(user);
//
// 		given(pageableResolver.supportsParameter(argThat(
// 			p -> p.getParameterType().equals(Pageable.class)
// 		))).willReturn(true);
// 		given(pageableResolver.resolveArgument(any(), any(), any(), any())).willReturn(PageRequest.of(0, 5));
//
// 		given(boardService.readBoardsWithUser(eq(user.id()), any(Pageable.class))).willReturn(pageResponseDto);
//
// 		mockMvc.perform(get("/api/boards/me")
// 				.param("page", "0")
// 				.param("size", "5"))
// 			.andExpect(status().isOk())
// 			.andExpect(jsonPath("$.result.status").value(200))
// 			.andExpect(jsonPath("$.data.content").isArray())
// 			.andExpect(jsonPath("$.data.content.length()").value(content.size()))
// 			.andExpect(jsonPath("$.data.page").value(1))
// 			.andExpect(jsonPath("$.data.size").value(5))
// 			.andExpect(jsonPath("$.data.totalElements").value(2))
// 			.andExpect(jsonPath("$.data.totalPages").value(1))
// 			.andExpect(jsonPath("$.data.content[0].title").value("title1"));
// 	}
// }
//

package cart.service;

import cart.dao.ProductDao;
import cart.domain.ProductEntity;
import cart.dto.RequestCreateProductDto;
import cart.dto.RequestUpdateProductDto;
import cart.dto.ResponseProductDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    void 상품을_조회한다() {
        //given
        when(productDao.findAll())
                .thenReturn(List.of(new ProductEntity(1L, "치킨", 1_000, "치킨 사진")));

        //when
        final List<ResponseProductDto> responseProductDtos = productService.findAll();

        //then
        assertSoftly(softly -> {
            softly.assertThat(responseProductDtos).hasSize(1);
            final ResponseProductDto responseProductDto = responseProductDtos.get(0);
            softly.assertThat(responseProductDto.getId()).isEqualTo(1L);
            softly.assertThat(responseProductDto.getName()).isEqualTo("치킨");
            softly.assertThat(responseProductDto.getPrice()).isEqualTo(1_000);
            softly.assertThat(responseProductDto.getImage()).isEqualTo("치킨 사진");
        });
    }

    @Test
    void 상품을_등록한다() {
        // given
        when(productDao.insert(any()))
                .thenReturn(1L);
        final RequestCreateProductDto requestCreateProductDto = new RequestCreateProductDto("치킨", 1000, "치킨 사진");

        // when
        final Long id = productService.insert(requestCreateProductDto);

        // then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 상품을_수정한다() {
        // given
        when(productDao.update(any(Long.class), any(ProductEntity.class)))
                .thenReturn(1);
        final RequestUpdateProductDto requestUpdateProductDto = new RequestUpdateProductDto(1L, "치킨", 1000, "치킨 사진");

        // when
        final int affectedRows = productService.update(1L, requestUpdateProductDto);

        // then
        assertThat(affectedRows).isEqualTo(1);
    }

    @Test
    void 존재하지_않은_상품을_수정하면_예외가_발생한다() {
        // given
        when(productDao.update(any(Long.class), any(ProductEntity.class)))
                .thenReturn(0);
        final RequestUpdateProductDto requestUpdateProductDto = new RequestUpdateProductDto(1L, "치킨", 1000, "치킨 사진");

        // expect
        assertThatThrownBy(() -> productService.update(1L, requestUpdateProductDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("접근하려는 데이터가 존재하지 않습니다.");
    }

    @Test
    void 상품을_삭제한다() {
        // given
        when(productDao.delete(any(Long.class)))
                .thenReturn(1);
        final Long id = 1L;

        // when
        final int affectedRows = productService.delete(id);

        // then
        assertThat(affectedRows).isEqualTo(1);
    }

    @Test
    void 존재하지_않은_상품을_삭제하면_예외가_발생한다() {
        // given
        when(productDao.delete(any(Long.class)))
                .thenReturn(0);
        final Long id = 1L;

        // expect
        assertThatThrownBy(() -> productService.delete(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("접근하려는 데이터가 존재하지 않습니다.");
    }
}

package reactive_backend.ability.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageCustomTest {

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        PageCustom<String> page = new PageCustom<>(1, 10, 5, List.of("item1", "item2"));

        assertEquals(1, page.getCurrentPage());
        assertEquals(10, page.getPageSize());
        assertEquals(5, page.getTotalPages());
        assertEquals(List.of("item1", "item2"), page.getItems());
    }

    @Test
    void defaultConstructor_shouldInitializeFieldsToNull() {
        PageCustom<String> page = new PageCustom<>();

        assertNull(page.getCurrentPage());
        assertNull(page.getPageSize());
        assertNull(page.getTotalPages());
        assertNull(page.getItems());
    }

    @Test
    void setCurrentPage_shouldUpdateCurrentPage() {
        PageCustom<String> page = new PageCustom<>();
        page.setCurrentPage(2);

        assertEquals(2, page.getCurrentPage());
    }

    @Test
    void setPageSize_shouldUpdatePageSize() {
        PageCustom<String> page = new PageCustom<>();
        page.setPageSize(20);

        assertEquals(20, page.getPageSize());
    }

    @Test
    void setTotalPages_shouldUpdateTotalPages() {
        PageCustom<String> page = new PageCustom<>();
        page.setTotalPages(10);

        assertEquals(10, page.getTotalPages());
    }

    @Test
    void setItems_shouldUpdateItems() {
        PageCustom<String> page = new PageCustom<>();
        List<String> items = List.of("item1", "item2");
        page.setItems(items);

        assertEquals(items, page.getItems());
    }

    @Test
    void getItems_shouldReturnEmptyListWhenItemsIsNull() {
        PageCustom<String> page = new PageCustom<>();

        assertTrue(page.getItems() == null || page.getItems().isEmpty());
    }
}
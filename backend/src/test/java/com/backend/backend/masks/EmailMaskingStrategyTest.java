package com.backend.backend.masks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class EmailMaskingStrategyTest {
    @Test
    public void testEmailMask() {
        UnifiedHeirarchicalObject o1 = new UnifiedHeirarchicalObject("key1", "john.doe@whoever.com");
        UnifiedHeirarchicalObject o2 = new UnifiedHeirarchicalObject("key2", "maryJane@whyMe.mail");
        List<UnifiedHeirarchicalObject> o_list = new ArrayList<>();
        o_list.add(o1);
        o_list.add(o2);

        MaskingStrategy emailMasking = new EmailMaskingStrategy(null);
        emailMasking.mask(o_list);

        assertEquals(o1.getValue(), "user@whoever.com");
        assertEquals(o2.getValue(), "user@whyMe.mail");
    }
}

package com.kodex.guide

import com.kodex.guide.data.service.WorkingHoursServiceImpl
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.LocalTime

class WorkingHoursServiceImplTest {
    private val service = WorkingHoursServiceImpl()
    private val noon = LocalTime.of(12, 0)
    private val night = LocalTime.of(3, 0)


    @Test fun `обычный день открыт`() = assertTrue(service.isOpenNow("09:00 - 20:00", noon))
    @Test fun `обычный день закрыт`() = assertFalse(service.isOpenNow("09:00 - 20:00", night))
    @Test fun `через полночь открыт ночью`() = assertTrue(service.isOpenNow("22:00 - 06:00", night))
    @Test fun `круглосуточно`() = assertTrue(service.isOpenNow("24/7", night))
    @Test fun `пустая строка закрыто`() = assertFalse(service.isOpenNow("", noon))
}
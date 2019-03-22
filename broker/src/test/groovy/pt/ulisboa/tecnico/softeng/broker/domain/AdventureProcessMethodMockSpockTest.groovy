package pt.ulisboa.tecnico.softeng.broker.domain

import org.joda.time.LocalDate
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import mockit.Expectations
import mockit.Mocked
import mockit.integration.junit4.JMockit
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type

@RunWith(JMockit.class)
class AdventureProcessMethodMockTest {
	@Shared def ACTIVITY_REFERENCE = "activityReference"
	@Shared def HOTEL_REFERENCE = "hotelReference"
	@Shared def PAYMENT_CONFIRMATION = "paymentConfirmation"
	@Shared def IBAN = "BK01987654321"
	@Shared def begin = new LocalDate(2016, 12, 19)
	@Shared def end = new LocalDate(2016, 12, 21)
	@Shared Broker broker
	@Shared BankInterface bankInterface
	//@Before
	def setUp() {
		bankInterface = mock()
		this.broker = new Broker("BR98", "Travel Light", bankInterface)
	}

	@Test
	def processWithNoExceptions(@Mocked final BankInterface bankInterface,
										@Mocked final HotelInterface hotelInterface, @Mocked final ActivityInterface activityInterface) {
		new Expectations() {
			{
				bankInterface.processPayment(IBAN, 300)
				this.result = PAYMENT_CONFIRMATION

				HotelInterface.reserveHotel(Type.SINGLE, AdventureProcessMethodMockTest.this.begin,
						AdventureProcessMethodMockTest.this.end)
				this.result = HOTEL_REFERENCE

				ActivityInterface.reserveActivity(AdventureProcessMethodMockTest.this.begin,
						AdventureProcessMethodMockTest.this.end, 20)
				this.result = ACTIVITY_REFERENCE
			}
		}

		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300)

		adventure.process()

		Assert.assertEquals(PAYMENT_CONFIRMATION, adventure.getBankPayment())
		Assert.assertEquals(HOTEL_REFERENCE, adventure.getRoomBooking())
		Assert.assertEquals(ACTIVITY_REFERENCE, adventure.getActivityBooking())
	}

	@After
	def tearDown() {
		Broker.brokers.clear()
	}

}

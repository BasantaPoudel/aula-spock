package pt.ulisboa.tecnico.softeng.broker.domain

import org.joda.time.LocalDate

import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type
import spock.lang.Specification

class AdventureProcessMethodMockSpockTest extends Specification {

    def ACTIVITY_REFERENCE = "activityReference"
    def HOTEL_REFERENCE = "hotelReference"
    def PAYMENT_CONFIRMATION = "paymentConfirmation"
    def IBAN = "BK01987654321"
    def begin = new LocalDate(2016, 12, 19)
    def end = new LocalDate(2016, 12, 21)
    def broker

    def bankInterface = Mock(BankInterface)
    def hotelInterface = Mock(HotelInterface)
    def activityInterface = Mock(ActivityInterface)

    def setup() {
        broker = new Broker("BR98", "Travel Light", bankInterface)
    }

    def 'process with no exceptions'() {
        given:
        bankInterface.processPayment(IBAN, 300) >> PAYMENT_CONFIRMATION
        hotelInterface.reserveHotel(Type.SINGLE, begin, end) >> HOTEL_REFERENCE
        activityInterface.reserveActivity(begin, end, 20) >> ACTIVITY_REFERENCE;

        and:
        def adventure = new Adventure(broker, begin, end, 20, IBAN, 300,
                hotelInterface, activityInterface)

        when:
        adventure.process()

        then:
        PAYMENT_CONFIRMATION == adventure.getBankPayment()
        HOTEL_REFERENCE == adventure.getRoomBooking()
        ACTIVITY_REFERENCE == adventure.getActivityBooking()
    }

    def cleanup() {
        Broker.brokers.clear()
    }
}

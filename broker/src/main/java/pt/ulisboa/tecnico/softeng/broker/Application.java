package pt.ulisboa.tecnico.softeng.broker;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;

public class Application {

	public static void main(String[] args) {
		System.out.println("Adventures!");

		Bank bank = new Bank("MoneyPlus", "BK01");
		Account account = new Account(bank, new Client(bank, "José dos Anzóis"));
		account.deposit(1000);

		Broker broker = new Broker("BR01", "Fun", new BankInterface());
		Adventure adventure = new Adventure(broker, new LocalDate(), new LocalDate(), 33, account.getIBAN(), 50,
				new HotelInterface(), new ActivityInterface());

		adventure.process();

		System.out.println("Your payment reference is " + adventure.getBankPayment() + " and you have "
				+ account.getBalance() + " euros left in your account");
	}

}

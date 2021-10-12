import java.lang.Exception
import java.text.DecimalFormat

var islive :Boolean = false
var name: String = ""
var pin: Int = 0
val df = DecimalFormat("#.##")

data class Customer(var accnumber: Int, var pin: Int, var user: String, var name: String, var bal: Double)

// main data to costumers
object CustomerList {
    var customerList = listOf(
        Customer(112223,1234,"dan","daniel", 500.0),
        Customer(334443,1221,"bob","bobot", 500.0),
        Customer(444212,1212,"ted","teddy", 500.0),
        Customer(322112,2323,"jan","janwel", 500.0)
    )
}
fun List<Customer>.filterByAccn(accnumber: Int) = this.filter { it.accnumber == accnumber } //filter and find Customer By Account number
fun List<Customer>.filterByUser(nameid: String) = this.filter { it.user == nameid } //filter and find Customer By Account Name

fun main() {
    if (!islive) {
        println("Welcome to BDO\nSimple ATM")
        islive = true
        algo()
    }else {
        algo()
    }
}
// Operation functions
fun operation(ope: Any, user: Customer){
    when(ope) {
        1 -> { withdraw(user)
            additional(user)
        }
        2 -> { deposit(user)
            additional(user)
        }
        3 -> { println("Your Balance is ${checkBal(user)} Petots")
            additional(user)
        }
        4 -> { sendMoney(user)
            additional(user)}
        5 -> { println("logout")
            pin = 0
            name = ""
            algo()
        }
        else -> {
            println("Invalid input!!")
            display()
            var select = Integer.valueOf(readLine())
            operation(select, user)
        }
    }
}

//main function
fun algo(){
    try {
        if (name.isEmpty()){
            print("Enter user:")
            name = readLine().toString()
        }
        //check data user if existing
        var getd = CustomerList.customerList.filterByUser(name)
        var user = getd.get(0)
            if (!name.isEmpty()) {
                print("Enter pin:")
                pin = Integer.valueOf(readLine())
                //check pin is match to user
                var validPin: Customer = getpin(pin)
                if (user.equals(validPin)) {
                    dispName(user)
                    display()
                    var select = Integer.valueOf(readLine())
                    operation(select, user)
                } else {
                    println("Wrong pin please try again")
                    name = ""
                    algo()
                }
            }
    } catch (e: Exception) {
        println("User does not exist")
        name = ""
        algo()
    }
}
//in addition, customer can choose to continue or exit
fun additional(user: Customer){

    println("Do you want another Operation? \nEnter 1: to continue. 2: to login again. 3: exit")
    var state = readLine()?.toInt()
    if (state == 1) {
        algo()
    } else if (state == 2) {
        println("Thank you for banking!")
        name = ""
        pin = 0
        algo()
    } else if (state == 3) {
        println("Shutting down in 2 sec")
        Thread.sleep(2_000)
        println("bye")
        System.exit(0)
    }
    else {
        println("Invalid input!!")
        additional(user)
    }
}
//displays the additional features
fun display(){
    println("Select operation you want to perform:\n 1: Withdraw. 2: Deposit. 3: Balance. 4: Send Money. 5: logout \")")
}
//withdraw features
fun withdraw(user: Customer) {
    println("Your Balance is ${checkBal(user)} Petots")
    print("Input Amount to Withdraw : ")
    var isAmount = false
    while (!isAmount) {
        try {
            val amount = readLine()
            val money = user.bal
            if (money >= (amount?.toDouble()!!)) {
                val balance = money - (amount.toDouble())
                user.bal = balance
                println("$amount petots deducted")
                println("Your Balance now is $balance Petots")
                isAmount = true
            } else {
                print("Please Input Amount Lower Than Your Balance : ")
            }
        } catch (e: Exception) {
            print("Please Input Amount : ")
        }
    }
}
//depo features
fun deposit(user: Customer){
    print("Input Amount to Deposit : ")
    var isAmount = false
    while (!isAmount) {
        try {
            val amount = readLine()
            val money = user.bal
            val balance = money + (amount?.toDouble()!!)
            user.bal = balance
            println(" Added $amount Petots successful")
            println("Your Balance is now ${checkBal(user)} Petots")
            isAmount = true
        }catch (e: Exception){
            println("Please Input Amount :")
        }
    }
}

//check bal features
fun checkBal(user: Customer): Any {
    return df.format(user.bal).toDouble()
}
//displays log-in name customer
fun dispName(user: Customer){
    var dname = user.name
    return println("Welcome $dname")
}
//check valid pin feature
fun getpin(pin: Int): Customer{
    var fpin =  Customer(0,0, "","",0.0)
    for(customer in CustomerList.customerList){
        if(customer.pin == pin){
            fpin = customer
        }
    }
    return fpin
}
// send money features
fun sendMoney(user: Customer){
    var isAccountNumber = false
    var accnumber = 0
    var accountName = ""
    val money = user.bal
    while (!isAccountNumber) {
        try {
            if(accnumber==0){
                print("Input Account Number of Receiver: ")
                accnumber = Integer.valueOf(readLine())
            }else {
                val user2 = CustomerList.customerList.filterByAccn(accnumber).last()
                if(accountName==""){
                    print("Input Account Name of Receiver: ")
                    accountName = readLine().toString()
                }
                else{
                    if (user2.name.equals(accountName, ignoreCase = true)){
                        try {
                            print("Input Amount to send to ${user2.name}: ")
                            val amount = readLine()

                            if(money >= (amount?.toDouble()!!)){
                                val balance = money - (amount.toDouble())
                                user2.bal = user2.bal + amount.toDouble()
                                user.bal = balance
                                println("$amount petots is Transfer to ${user2.name} Successfully")
                                println("Your Balance is now $balance Petots.")
                                isAccountNumber = true
                            }else{
                                print("Please Input Amount Lower Than Your Balance :")
                            }
                        }catch (e: Exception){
                            print("Please Input A Number: ")
                        }
                    }else{
                        println("Account Name of ${user2.accnumber} is Incorrect")
                        accountName=""
                    }
                }
            }
        }catch (e: Exception){
            println("User Not Found")
            accnumber=0
        }
    }
}

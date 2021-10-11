import java.lang.Exception
import java.text.DecimalFormat


var amount = 0
var islive :Boolean = false
var name: String? = null
var pin: Int = 0
val df = DecimalFormat("#.##")

data class Customer(var accnumber: Int,var pin: Int,var nameid: String,var fname: String, var bal: Double)

object CustomerList {
    var customerList = listOf(
        Customer(112223,1234,"dan","daniel tann", 500.0),
        Customer(334443,1221,"bob","bob marley", 500.0),
        Customer(444212,1212,"ted","teddy bear", 500.0),
        Customer(322112,2323,"jan","janwel cruz", 500.0)
    )
}
fun List<Customer>.filterByAccn(accnumber: Int) = this.filter { it.accnumber == accnumber } //filter and find Customer By Pin
fun List<Customer>.filterByPin(pin: Int) = this.filter { it.pin == pin }
fun List<Customer>.filterByUser(nameid: String) = this.filter { it.nameid == nameid } //filter and find Customer By Account Name

fun main() {
    if (!islive) {
        println("Welcome to BDO\nSimple ATM")
        islive = true
        algo()
    }else {
        algo()
    }
}

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
            name = null
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
fun algo(){
    try {
        if (name == null){
        print("Enter user:")
        name = readLine().toString()
        }
        var lg = name?.let { CustomerList.customerList.filterByUser(it) }
        if (lg != null) {
            if (lg.last()!=null) {
                print("Enter pin:")
                pin = Integer.valueOf(readLine())
                var validPin: Customer = getpin(pin)
                var user = pin?.let { CustomerList.customerList.filterByPin(it) }
                if (user != null) {
                    if (lg.last()!=null && pin.equals(validPin.pin)) {
                        displayname(user.last())
                        display()
                        var select = Integer.valueOf(readLine())
                        operation(select,user.last())
                    } else{
                        println("Wrong pass please try again")
                        algo()
                        name = null
                    }
                }
            }
        }
    } catch (e: Exception) {
        println("User does not exist")
        name = null
        algo()
    }
}
fun additional(user: Customer){

    println("Do you want another Operation? \nEnter 1: to continue. 2: to login again. 3: exit")
    var state = readLine()?.toInt()
    if (state == 1) {
        algo()
    } else if (state == 2) {
        println("Thank you for banking!")
        name = null
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
fun display(){
    println("Select operation you want to perform:\n 1: Withdraw. 2: Deposit. 3: Balance. 4: Send Money. 5: logout \")")
}
fun withdraw(user: Customer) {
    println("Your Balance is ${checkBal(user)} Petots")
    print("Input Amount to Withdraw :")
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
                println("Please Input Amount Lower Than Your Balance :")
            }
        } catch (e: Exception) {
            print("Please Input Amount :")
        }
    }
}
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


fun checkBal(user: Customer): Any {
    return df.format(user.bal).toDouble()
}
fun displayname(user: Customer){
  var dname = user.fname
   return println("Welcome $dname")
}
fun getpin(pin: Int): Customer{
    var result =  Customer(0,0, "","",0.0)
    for(customer in CustomerList.customerList){
        if(customer.pin == pin){
            result = customer
        }
    }
    return result
}
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
                    if (user2.fname.equals(accountName, ignoreCase = true)){
                        try {
                            print("Input Amount to send to ${user2.fname}: ")
                            val amount = readLine()

                            if(money >= (amount?.toDouble()!!)){
                                val balance = money - (amount.toDouble())
                                user2.bal = user2.bal + amount.toDouble()
                                user.bal = balance
                                println("$amount petots is Transfer to ${user2.fname} Successfully")
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
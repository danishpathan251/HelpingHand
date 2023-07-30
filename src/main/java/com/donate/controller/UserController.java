package com.donate.controller;
//import com.razorpay.*;
import com.donate.dao.OrganizationRepository;
import com.donate.dao.TransactionRepository;
import com.donate.dao.UserRepository;
import com.donate.entities.Organization;
import com.donate.entities.Transaction;
import com.donate.entities.User;
import com.donate.services.JwtService;
import com.donate.services.TransactionService;
import com.donate.services.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/home")
public class UserController {

            @Autowired
            private UserRepository userRepository;

            @Autowired
            private OrganizationRepository organizationRepository;


            @Autowired
            private TransactionRepository transactionRepository;

            @Autowired
            private JwtService jwtService;

            @Autowired
            private UserService userService;
            @Autowired
            private AuthenticationManager authenticationManager;

            @Autowired
            private TransactionService transactionService;

            @GetMapping("/getMessage")
            public String getMessage(){
                return "This is for Testing Purpose";
            }

            @CrossOrigin(origins = "*")
            @PostMapping("/addUser")
            public User addUser(@RequestBody User user){
                return userService.registerUser(user);
            }



            @CrossOrigin(origins = "*")
            @PostMapping("/addTransaction")
            public Transaction addTransaction(@RequestBody Transaction transaction){
                return transactionRepository.save(transaction);
             }


        @CrossOrigin(origins = "*")
        @PostMapping("/authentication")
        public String authenticationAndGetToken(@RequestBody User user){

            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if(authenticate.isAuthenticated()){
                return jwtService.generateToken(user.getUsername());

            }
            else{
                throw new UsernameNotFoundException("invalid User");
            }

        }

        @CrossOrigin(origins = "*")
        @GetMapping("getuser/{username}")
        public ResponseEntity<User> getUser(@PathVariable String username){

            Optional<User> userinfo =userRepository.findByUsername(username);

            if(userinfo.isPresent()){
                return ResponseEntity.ok(userinfo.get());
            }
            else{
                return ResponseEntity.notFound().build();
            }
        }



        @CrossOrigin(origins = "*")
        @PostMapping("/donate")
        public String createOrder(@RequestBody Transaction transaction) throws RazorpayException {

            LocalDateTime currentDateTime = LocalDateTime.now();
            Double amount = transaction.getAmount();
                System.out.println(amount);

            transaction.setStatus("Cancel");
            transactionRepository.save(transaction);

            RazorpayClient razorpayClient = new RazorpayClient("rzp_test_hqSbc7fpHYxVPK", "YaBdxyNdP9OsVCEW4O1gVRCZ");

            try {
                // Create an order request
                JSONObject orderRequest = new JSONObject();
                orderRequest.put("amount", amount * 100); // Convert amount to paise (currency subunit)
                orderRequest.put("currency", "INR"); // Set the currency code
                orderRequest.put("receipt", "order_receipt"); // Set a unique receipt identifier

                // Send the order request to Razorpay API
                Order order = razorpayClient.Orders.create(orderRequest);

                // Retrieve the order ID
                String orderId = order.get("id");

    //


                // Perform any additional operations with the order or store the order ID in your system
                // ...

                return orderId; // Return the order ID as the response
            } catch (RazorpayException e) {
                // Handle any exceptions that occur during the Razorpay API request
                // ...

                return "Error"; // Return an error response
            }
        }

    //    @PostMapping("/addDonation")
    //    @CrossOrigin(origins = "*")
    //    public ResponseEntity<String> saveEmployee(@RequestBody Donation donation){
    //            donationRepository.save(donation);
    //            return ResponseEntity.ok("saved");
    //    }


    //    Getmapping

        @GetMapping("/getOrganisation")
        @CrossOrigin(origins = "*")
        public List<Organization> getOrganisations(){
                return organizationRepository.findAll();

        }

        @GetMapping("/getTransaction")
        @CrossOrigin(origins = "*")
        public List<Transaction> getALltransaction(){
                return transactionRepository.findAll();
        }

        @GetMapping("/transaction/{userId}")
        @CrossOrigin(origins =  "*")
        public List<Transaction> getTransactionsByUserId(@PathVariable Long userId) {
            return transactionService.getTransactionsByUserId(userId);
        }

//        @PutMapping("/forgot_password")
//        public ResponseEntity<String> forgotPassword(@RequestParam String email){
//                return new ResponseEntity<>(userService.forgotPassword(email), HttpStatus);
//        }

}

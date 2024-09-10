/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.appointmentmanagementsystem;

/**
 *
 * @author hp
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Base Doctor class
class Doctor {
    private String name;
    private String specialization;

    public Doctor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    // Method to display availability (Overridden in derived classes)
    public void displayAvailability() {
        System.out.println("Availability of doctor: " + name);
    }
}

// Derived class: General Practitioner
class Psychologist extends Doctor {
    public Psychologist(String name) {
        super(name, "Psychologist");
    }

    @Override
    public void displayAvailability() {
        System.out.println("Dr. " + getName() + " (Psychologist) is available for walk-ins.");
    }
}

// Derived class: Specialist
class Cardiologist extends Doctor {
    public Cardiologist(String name) {
        super(name, "Cardiologist");
    }

    @Override
    public void displayAvailability() {
        System.out.println("Dr. " + getName() + " (Cardiologist) needs appointment confirmation.");
    }
}

// Patient class
class Patient {
    private String name;
    private String contactInfo;

    public Patient(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }
}

// Appointment class
class Appointment {
    private Doctor doctor;
    private Patient patient;
    private String date;

    public Appointment(Doctor doctor, Patient patient, String date) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Appointment: " + patient.getName() + " with Dr. " + doctor.getName() + " on " + date;
    }

    // Save appointment to file
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("appointments.txt", true))) {
            writer.write(toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Appointment Management System
public class AppointmentManagementSystem {
    private static List<Doctor> doctors = new ArrayList<>();
    private static List<Patient> patients = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    // Load initial data
    static {
        doctors.add(new Psychologist("Shimi"));
        doctors.add(new Cardiologist("Hoyeon Jung"));
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nAppointment Management System");
            System.out.println("1. Input Patient");
            System.out.println("2. Available Doctors");
            System.out.println("3. Book Appointment");
            System.out.println("4. View Appointments");
            System.out.println("5. Exit");
            System.out.print("Select: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    viewAvailableDoctors();
                    break;
                case 3:
                    bookAppointment();
                    break;
                case 4:
                    viewAppointments();
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid. Try again.");
            }
        }
    }

    // Register a new patient
    private static void registerPatient() {
        System.out.print("Input patient name: ");
        String name = scanner.nextLine();
        System.out.print("Input contact: ");
        String contactInfo = scanner.nextLine();
        patients.add(new Patient(name, contactInfo));
        System.out.println("Patient " + name + " registered successfully.");
    }

    // View available doctors
    private static void viewAvailableDoctors() {
        System.out.println("\nAvailable Doctors:");
        for (Doctor doctor : doctors) {
            doctor.displayAvailability();
        }
    }

    // Book an appointment
    private static void bookAppointment() {
        System.out.println("\nBooking appointment");
        System.out.print("Input patient: ");
        String patientName = scanner.nextLine();

        // Find patient by name
        Patient patient = null;
        for (Patient p : patients) {
            if (p.getName().equalsIgnoreCase(patientName)) {
                patient = p;
                break;
            }
        }
        if (patient == null) {
            System.out.println("Not found. Register first.");
            return;
        }

        System.out.println("Select doctor:");
        for (int i = 0; i < doctors.size(); i++) {
            System.out.println((i + 1) + ". Dr. " + doctors.get(i).getName() + " (" + doctors.get(i).getSpecialization() + ")");
        }
        int doctorIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline

        if (doctorIndex < 0 || doctorIndex >= doctors.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Doctor doctor = doctors.get(doctorIndex);
        System.out.print("Input appointment date (DD-MM-YY): ");
        String date = scanner.nextLine();

        Appointment appointment = new Appointment(doctor, patient, date);
        appointment.saveToFile();
        System.out.println("Appointment successful.");
    }

    // View all appointments from file
    private static void viewAppointments() {
        System.out.println("\nAll Appointments:");
        try (BufferedReader reader = new BufferedReader(new FileReader("appointments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("There's no appointment.");
        }
    }
}


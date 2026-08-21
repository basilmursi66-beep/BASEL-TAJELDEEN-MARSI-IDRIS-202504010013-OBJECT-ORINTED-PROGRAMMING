// Centralized validation so every dialog gives the same, specific error
// messages instead of a generic "invalid input."
public class Validator {

    // Returns null if valid, or a specific error message if not.

    public static String validateName(String name) {
        if (name == null || name.trim().isEmpty()) return "Name is required.";
        if (name.trim().length() < 2) return "Name must be at least 2 characters.";
        if (!name.matches("[a-zA-Z '\\-]+")) return "Name can only contain letters, spaces, apostrophes, and hyphens.";
        return null;
    }

    public static String validateAge(String ageText) {
        if (ageText == null || ageText.trim().isEmpty()) return "Age is required.";
        if (!ageText.matches("[0-9]+")) return "Age must be a whole number.";
        int age = Integer.parseInt(ageText);
        if (age < 15 || age > 100) return "Age must be between 15 and 100.";
        return null;
    }

    public static String validateStudentId(String id) {
        if (id == null || id.trim().isEmpty()) return "Student ID is required.";
        if (!id.matches("[a-zA-Z0-9]+")) return "Student ID can only contain letters and numbers, no spaces or symbols.";
        if (id.length() < 3 || id.length() > 12) return "Student ID must be between 3 and 12 characters.";
        return null;
    }

    public static String validatePassword(String password) {
        if (password == null || password.isEmpty()) return "Password is required.";
        if (password.length() < 4) return "Password must be at least 4 characters.";
        if (password.contains(" ")) return "Password cannot contain spaces.";
        return null;
    }

    public static String validateCourseCode(String code) {
        if (code == null || code.trim().isEmpty()) return "Course code is required.";
        if (!code.matches("[a-zA-Z0-9]+")) return "Course code can only contain letters and numbers.";
        if (code.length() < 2 || code.length() > 10) return "Course code must be between 2 and 10 characters.";
        return null;
    }

    public static String validateCourseName(String name) {
        if (name == null || name.trim().isEmpty()) return "Course name is required.";
        if (name.trim().length() < 2) return "Course name must be at least 2 characters.";
        return null;
    }

    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) return "Email is required.";
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            return "Enter a valid email address (e.g. name@example.com).";
        }
        return null;
    }

    public static String validateFeeAmount(String feeText) {
        // Fee amount is optional. A blank value means no fee has been specified.
        if (feeText == null || feeText.trim().isEmpty()) return null;
        if (!feeText.matches("[0-9]+(\\.[0-9]{1,2})?")) return "Enter a valid amount (e.g. 1500 or 1500.50).";
        return null;
    }
    public static String validateCapacity(String capacityText) {
        if (capacityText == null || capacityText.trim().isEmpty()) return "Capacity is required.";
        if (!capacityText.matches("[0-9]+")) return "Capacity must be a whole number.";
        int capacity = Integer.parseInt(capacityText);
        if (capacity <= 0) return "Capacity must be at least 1.";
        if (capacity > 500) return "Capacity seems unrealistically high (max 500).";
        return null;
    }
}

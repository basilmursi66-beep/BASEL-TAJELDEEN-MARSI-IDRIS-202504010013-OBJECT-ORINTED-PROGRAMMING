// Any entity in the system that can owe and pay a fee implements this.
// Currently only Student does, but the interface exists independently of
// that class - anything added later (e.g. a Course lab fee) could implement
// it too without touching Student's code.
public interface Payable {

    double getFeeAmount();
    boolean isFeePaid();
    void markAsPaid();
}

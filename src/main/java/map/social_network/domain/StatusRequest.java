package map.social_network.domain;

public enum StatusRequest {
    PENDING {
        @Override
        public String toString() {
            return "pending";
        }},
    REJECTED {
        @Override
        public String toString() {
            return "rejected";
        }},

    APPROVED{
        @Override
        public String toString() {
            return "approved";
        }
    }
}

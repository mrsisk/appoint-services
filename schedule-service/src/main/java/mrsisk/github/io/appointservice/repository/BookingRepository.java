package mrsisk.github.io.appointservice.repository;

import mrsisk.github.io.appointservice.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}

package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Addressee;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddresseeRepository extends LongJpaRepository<Addressee> {

    List<Addressee> findByNameIgnoreCaseLikeOrEmailIgnoreCaseLike(String name, String email);

    List<Addressee> findByNameIgnoreCaseLike(String name);

    List<Addressee> findByEmailIgnoreCaseLike(String email);

    List<Addressee> findByWhenDeletedIsNull(Pageable pageable);

    List<Addressee> findByDepartment_Id(Long departmentId);

}

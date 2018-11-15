/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: BOF VIP-Service
 *
 * $Id: ProductRepository.java 2018-11-14 10:09 tkachenko $
 *****************************************************************/
package com.noname.learningSpring.repositories;

import com.noname.learningSpring.entities.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

}

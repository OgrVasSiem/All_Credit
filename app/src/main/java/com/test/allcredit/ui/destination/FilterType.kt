package com.test.allcredit.ui.destination

import com.test.allcredit.R

enum class FilterType(val labelId: Int) {
    Rating(labelId = R.string.rating_filter),
    Rate(labelId = R.string.rate_filter),
    Sum(labelId = R.string.sum_filter),
    Term(labelId = R.string.term_filter)
}
package com.example.onlineshop.utils

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

const val CONSUMER_KEY = "ck_3fb6c37fed38fc42d9b28369c0cbdf8063fcf61e"
const val CONSUMER_SECRET = "cs_3cb4d01706cca4d74960048e8d7169f3ae046b39"
const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"

const val PAGE_SIZE = 20
const val MAX_SIZE = 200
const val INITIAL_SIZE = 30
const val PRE_FETCH_DISTANCE = 5

const val STARTING_PAGE_INDEX = 1

val glideDiskCacheStrategy = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

const val EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
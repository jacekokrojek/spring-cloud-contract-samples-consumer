package com.example.unused;

import com.example.Sample;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class SampleTest {

    @Test
    void sum() {
    	BDDAssertions.then(Sample.sum(1,3)).isEqualTo(4);
    }
}
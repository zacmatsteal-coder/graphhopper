/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.graphhopper.routing;

import com.graphhopper.GHRequest;
import com.graphhopper.config.Profile;
import com.graphhopper.routing.lm.LandmarkStorage;
import com.graphhopper.storage.RoutingCHGraph;
import com.graphhopper.util.PMap;
import com.graphhopper.util.Parameters;

import static com.graphhopper.util.Parameters.Algorithms.ALT_ROUTE;
import static com.graphhopper.util.Parameters.Algorithms.ROUND_TRIP;
import static com.graphhopper.util.Parameters.Routing.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;;

public class RoutingCheckNoLegacyParametersTest {
    
    @Mock
    private GHRequest mockedGHRequest;
    @Mock
    private PMap mockedPMap;

    static private Router router;

    // Call the Router constructor class with the necessary mocks
    @BeforeAll
    static public void setupRouter() {
        Map<String, RoutingCHGraph> mockedCHGraphs = Mockito.mock(Map.class);
        Map<String, LandmarkStorage> mockedLandmarks = Mockito.mock(Map.class);
        Map<String, Profile> mockedProfilesByName = Mockito.mock(Map.class);

        when(mockedCHGraphs.isEmpty()).thenReturn(false);
        when(mockedLandmarks.isEmpty()).thenReturn(false);
        when(mockedProfilesByName.keySet()).thenReturn(Collections.emptySet());

        router = new Router(null, null, null, mockedProfilesByName, null, null, null, null, mockedCHGraphs, mockedLandmarks);
    }

    @BeforeEach
    public void setup() {
        mockedGHRequest = Mockito.mock(GHRequest.class);
        mockedPMap = Mockito.mock(PMap.class);

        when(mockedGHRequest.getHints()).thenReturn(mockedPMap);
        when(mockedPMap.has(anyString())).thenReturn(false);
    }

    // Happy path
    @Test
    public void testNoLegacyParameters() {
        assertDoesNotThrow(() -> {
            router.checkNoLegacyParameters(mockedGHRequest);
        },
        "The method \"checkNoLegacyParameters\" should not throw any exception if there are no legacy parameters");
    }

    @Test
    public void testVehicleParameter() {
        // Arrange
        when(mockedPMap.has("vehicle")).thenReturn(true);

        // Act (and Assert a tiny bit)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            router.checkNoLegacyParameters(mockedGHRequest);
        },
        "The method \"checkNoLegacyParameters\" should throw an exception if there is the vehicle legacy parameter");

        // Assert
        assertTrue(exception.getMessage().contains("vehicle"),
        "The method \"checkNoLegacyParameters\" should throw an exception for the vehicle legacy parameter");
    }

    @Test
    public void testWeightParameter() {
        // Arrange
        when(mockedPMap.has("weighting")).thenReturn(true);

        // Act and Assert (and Assert a tad bit)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            router.checkNoLegacyParameters(mockedGHRequest);
        },
        "The method \"checkNoLegacyParameters\" should throw an exception if there is the weighting legacy parameter");

        // Assert
        assertTrue(exception.getMessage().contains("weighting"),
        "The method \"checkNoLegacyParameters\" should throw an exception for the weighting legacy parameter");
    }

    @Test
    public void testTurnCostsParameter() {
        // Arrange
        when(mockedPMap.has(Parameters.Routing.TURN_COSTS)).thenReturn(true);

        // Act and Assert (and Assert a bit bit)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            router.checkNoLegacyParameters(mockedGHRequest);
        },
        "The method \"checkNoLegacyParameters\" should throw an exception if there is the turn_costs legacy parameter");

        // Assert
        assertTrue(exception.getMessage().contains("turn_costs"),
        "The method \"checkNoLegacyParameters\" should throw an exception for the turn_costs legacy parameter");
    }

    @Test
    public void testEdgeBasedParameter() {
        // Arrange
        when(mockedPMap.has(Parameters.Routing.EDGE_BASED)).thenReturn(true);

        // Act and Assert (and Assert a lot bit)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            router.checkNoLegacyParameters(mockedGHRequest);
        },
        "The method \"checkNoLegacyParameters\" should throw an exception if there is the edge_based legacy parameter");

        // Assert
        assertTrue(exception.getMessage().contains("edge_based"),
        "The method \"checkNoLegacyParameters\" should throw an exception for the edge_based legacy parameter");
    }

    // Little test made to verify that the rickroll works well
    @Test
    public void testFail() {
        assertTrue(false);
    }
}

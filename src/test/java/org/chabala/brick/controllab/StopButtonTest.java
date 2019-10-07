/*
 * Copyright © 2016 Greg Chabala
 *
 * This file is part of brick-control-lab.
 *
 * brick-control-lab is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * brick-control-lab is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with brick-control-lab.  If not, see http://www.gnu.org/licenses/.
 */
package org.chabala.brick.controllab;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.chabala.brick.controllab.Protocol.STOP_DEPRESSED;
import static org.chabala.brick.controllab.Protocol.STOP_RELEASED;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.mockito.Mockito.*;

/**
 * Testing the {@link StopButton}.
 */
public class StopButtonTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private InputManager inputManager;

    @Mock
    private StopButtonListener stopButtonListener;

    private StopButton stopButton;
    private ByteConsumer stopButtonCallback;

    @Before
    public void setUp() {
        ArgumentCaptor<ByteConsumer> captor = ArgumentCaptor.forClass(ByteConsumer.class);
        stopButton = new StopButton(inputManager);
        verify(inputManager).setStopButtonCallback(captor.capture());
        stopButtonCallback = captor.getValue();
    }

    @After
    public void tearDown() {
        stopButton = null;
        stopButtonCallback = null;
    }

    @Test
    public void testStopButtonIsInitiallyNotStopped() {
        assertThat(stopButton.isStopDepressed(), is(false));
    }

    @Test
    public void testNoEventsWhenNotStoppedAndStopNotPressed() {
        assumeThat(stopButton.isStopDepressed(), is(false));
        stopButton.addListener(stopButtonListener);
        stopButtonCallback.accept(STOP_RELEASED);
        stopButton.removeListener(stopButtonListener);

        assertThat(stopButton.isStopDepressed(), is(false));
        verify(stopButtonListener, never()).stopButtonPressed(any());
        verify(stopButtonListener, never()).stopButtonReleased(any());
    }

    @Test
    public void testStopPressedEventWhenNotStoppedAndStopPressed() {
        assumeThat(stopButton.isStopDepressed(), is(false));
        stopButton.addListener(stopButtonListener);
        stopButtonCallback.accept(STOP_DEPRESSED);
        stopButton.removeListener(stopButtonListener);

        assertThat(stopButton.isStopDepressed(), is(true));
        ArgumentCaptor<StopButtonEvent> captor = ArgumentCaptor.forClass(StopButtonEvent.class);
        verify(stopButtonListener, times(1)).stopButtonPressed(captor.capture());
        StopButtonEvent stopButtonEvent = captor.getValue();
        assertThat(stopButtonEvent.getRawValue(), is(STOP_DEPRESSED));
        assertThat(stopButtonEvent + "", containsString("0x10"));
        verify(stopButtonListener, never()).stopButtonReleased(any());
    }

    @Test
    public void testNoEventsWhenStoppedAndStopNotReleased() {
        assumeThat(stopButton.isStopDepressed(), is(false));
        stopButtonCallback.accept(STOP_DEPRESSED);
        assumeThat(stopButton.isStopDepressed(), is(true));

        stopButton.addListener(stopButtonListener);
        stopButtonCallback.accept(STOP_DEPRESSED);
        stopButton.removeListener(stopButtonListener);

        assertThat(stopButton.isStopDepressed(), is(true));
        verify(stopButtonListener, never()).stopButtonPressed(any());
        verify(stopButtonListener, never()).stopButtonReleased(any());
    }

    @Test
    public void testStopReleasedEventWhenStoppedAndStopReleased() {
        assumeThat(stopButton.isStopDepressed(), is(false));
        stopButtonCallback.accept(STOP_DEPRESSED);
        assumeThat(stopButton.isStopDepressed(), is(true));

        stopButton.addListener(stopButtonListener);
        stopButtonCallback.accept(STOP_RELEASED);
        stopButton.removeListener(stopButtonListener);

        assertThat(stopButton.isStopDepressed(), is(false));
        verify(stopButtonListener, never()).stopButtonPressed(any());
        ArgumentCaptor<StopButtonEvent> captor = ArgumentCaptor.forClass(StopButtonEvent.class);
        verify(stopButtonListener, times(1)).stopButtonReleased(captor.capture());
        StopButtonEvent stopButtonEvent = captor.getValue();
        assertThat(stopButtonEvent.getRawValue(), is(STOP_RELEASED));
    }
}

/*
 * Copyright 2015 Stuart Kent
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package amplify.prompt;

import amplify.helpers.BaseTest;
import com.example.amplify.lib.prompt.PromptPresenter;
import com.example.amplify.lib.prompt.interfaces.IPromptPresenter;
import com.example.amplify.lib.prompt.interfaces.IPromptView;
import com.example.amplify.lib.tracking.interfaces.IEvent;
import com.example.amplify.lib.tracking.interfaces.IEventListener;
import ohos.rpc.RemoteException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.Mock;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.*;

/**
 * PromptPresenterTest extends BaseTest
 */
public class PromptPresenterTest extends BaseTest {

    private PromptPresenter promptPresenter;

    @Mock
    private IEventListener mockEventListener;

    @Mock
    private IPromptView mockPromptView;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Override
    public void localSetUp() {
        promptPresenter = new PromptPresenter(mockEventListener, mockPromptView);
    }

    @Test
    public void testThatPresenterInstructsPromptViewToQueryUserOpinionWhenStarted() throws RemoteException {
        // Act
        promptPresenter.start();

        // Assert
        final InOrder inOrder = inOrder(mockPromptView);
        inOrder.verify(mockPromptView).queryUserOpinion(anyBoolean());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThatPresenterInstructsPromptViewToRequestPositiveFeedbackWhenUserReportsPositiveOpinion() throws RemoteException {
        // Act
        promptPresenter.reportUserOpinion(IPromptPresenter.UserOpinion.POSITIVE);

        // Assert
        final InOrder inOrder = inOrder(mockPromptView);
        inOrder.verify(mockPromptView).requestPositiveFeedback();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThatPresenterInstructsPromptViewToRequestCriticalFeedbackWhenUserReportsCriticalOpinion() throws RemoteException {
        // Act
        promptPresenter.reportUserOpinion(IPromptPresenter.UserOpinion.CRITICAL);

        // Assert
        final InOrder inOrder = inOrder(mockPromptView);
        inOrder.verify(mockPromptView).requestCriticalFeedback();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThatPresenterThrowsCorrectExceptionIfUserFeedbackActionIsSuppliedBeforeUserOpinion() throws RemoteException {
        // Arrange
        final IPromptPresenter.UserFeedbackAction anyValidUserFeedbackAction
                = IPromptPresenter.UserFeedbackAction.AGREED;

        exception.expect(IllegalStateException.class);
        exception.expectMessage("User opinion must be set before this method is called.");

        // Act
        promptPresenter.reportUserFeedbackAction(anyValidUserFeedbackAction);
    }

    @Test
    public void testThatPresenterInstructsPromptViewToShowProvidedThanksViewWhenUserAgreesToGivePositiveFeedback() throws RemoteException {
        // Arrange
        when(mockPromptView.providesThanksView()).thenReturn(true);

        // Act
        promptPresenter.reportUserOpinion(IPromptPresenter.UserOpinion.POSITIVE);
        promptPresenter.reportUserFeedbackAction(IPromptPresenter.UserFeedbackAction.AGREED);

        // Assert
        final InOrder inOrder = inOrder(mockPromptView);
        inOrder.verify(mockPromptView).thankUser(anyBoolean());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThatPresenterInstructsPromptViewToDismissWhenUserAgreesToGivePositiveFeedbackAndNoThanksViewProvided() throws RemoteException {
        // Arrange
        when(mockPromptView.providesThanksView()).thenReturn(false);

        // Act
        promptPresenter.reportUserOpinion(IPromptPresenter.UserOpinion.POSITIVE);
        promptPresenter.reportUserFeedbackAction(IPromptPresenter.UserFeedbackAction.AGREED);

        // Assert
        final InOrder inOrder = inOrder(mockPromptView);
        inOrder.verify(mockPromptView).dismiss(anyBoolean());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThatPresenterInstructsPromptViewToDismissWhenUserDeclinesToGivePositiveFeedback() throws RemoteException {
        // Act
        promptPresenter.reportUserOpinion(IPromptPresenter.UserOpinion.POSITIVE);
        promptPresenter.reportUserFeedbackAction(IPromptPresenter.UserFeedbackAction.DECLINED);

        // Assert
        final InOrder inOrder = inOrder(mockPromptView);
        inOrder.verify(mockPromptView).dismiss(anyBoolean());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThatPresenterInstructsPromptViewToShowProvidedThanksViewWhenUserAgreesToGiveCriticalFeedback() throws RemoteException {
        // Arrange
        when(mockPromptView.providesThanksView()).thenReturn(true);

        // Act
        promptPresenter.reportUserOpinion(IPromptPresenter.UserOpinion.CRITICAL);
        promptPresenter.reportUserFeedbackAction(IPromptPresenter.UserFeedbackAction.AGREED);

        // Assert
        final InOrder inOrder = inOrder(mockPromptView);
        inOrder.verify(mockPromptView).thankUser(anyBoolean());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThatPresenterInstructsPromptViewToDismissWhenUserAgreesToGiveCriticalFeedbackAndNoThanksViewProvided() throws RemoteException {
        // Arrange
        when(mockPromptView.providesThanksView()).thenReturn(false);

        // Act
        promptPresenter.reportUserOpinion(IPromptPresenter.UserOpinion.CRITICAL);
        promptPresenter.reportUserFeedbackAction(IPromptPresenter.UserFeedbackAction.AGREED);

        // Assert
        final InOrder inOrder = inOrder(mockPromptView);
        inOrder.verify(mockPromptView).dismiss(anyBoolean());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThatPresenterInstructsPromptViewToDismissWhenUserDeclinesToGiveCriticalFeedback() throws RemoteException {
        // Act
        promptPresenter.reportUserOpinion(IPromptPresenter.UserOpinion.CRITICAL);
        promptPresenter.reportUserFeedbackAction(IPromptPresenter.UserFeedbackAction.DECLINED);

        // Assert
        final InOrder inOrder = inOrder(mockPromptView);
        inOrder.verify(mockPromptView).dismiss(anyBoolean());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThatPromptPresenterPassesReceivedEventsToInjectedEventListener() throws RemoteException {
        // Arrange
        final IEvent mockEvent = mock(IEvent.class);

        // Act
        promptPresenter.notifyEventTriggered(mockEvent);

        // Assert
        verify(mockEventListener).notifyEventTriggered(mockEvent);
    }

    @Test
    public void testThatPromptPresenterPassesReceivedEventsToExtraEventListeners() throws RemoteException {
        // Arrange
        final IEventListener extraMockEventListener = mock(IEventListener.class);
        promptPresenter.addPromptEventListener(extraMockEventListener);

        final IEvent mockEvent = mock(IEvent.class);

        // Act
        promptPresenter.notifyEventTriggered(mockEvent);

        // Assert
        verify(extraMockEventListener).notifyEventTriggered(mockEvent);
    }

}

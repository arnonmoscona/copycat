/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.raft.state;

import net.kuujo.copycat.cluster.MessageHandler;
import net.kuujo.copycat.raft.rpc.*;
import net.kuujo.copycat.util.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * Abstract state.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
abstract class AbstractState implements MessageHandler<Request, Response>, Managed<AbstractState> {
  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  protected final RaftContext context;
  private volatile boolean open;

  protected AbstractState(RaftContext context) {
    this.context = context;
  }

  /**
   * Returns an exceptional future with the given exception.
   */
  protected <T> CompletableFuture<T> exceptionalFuture(Throwable t) {
    CompletableFuture<T> future = new CompletableFuture<>();
    future.completeExceptionally(t);
    return future;
  }

  /**
   * Returns the Copycat state represented by this state.
   *
   * @return The Copycat state represented by this state.
   */
  public abstract RaftState type();

  /**
   * Logs a request.
   */
  protected final <R extends Request> R logRequest(R request) {
    LOGGER.debug("{} - Received {}", context.getCluster().member().id(), request);
    return request;
  }

  /**
   * Logs a response.
   */
  protected final <R extends Response> R logResponse(R response) {
    LOGGER.debug("{} - Sent {}", context.getCluster().member().id(), response);
    return response;
  }

  @Override
  public CompletableFuture<AbstractState> open() {
    context.checkThread();
    context.getCluster().member().registerHandler(context.getTopic(), this);
    open = true;
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public boolean isOpen() {
    return open;
  }

  @Override
  public CompletableFuture<Response> handle(Request request) {
    context.checkThread();
    switch (request.type()) {
      case JOIN:
        return join((JoinRequest) request).thenApply(AbstractState::castResponse);
      case PROMOTE:
        return promote((PromoteRequest) request).thenApply(AbstractState::castResponse);
      case DEMOTE:
        return demote((DemoteRequest) request).thenApply(AbstractState::castResponse);
      case LEAVE:
        return leave((LeaveRequest) request).thenApply(AbstractState::castResponse);
      case REGISTER:
        return register((RegisterRequest) request).thenApply(AbstractState::castResponse);
      case KEEP_ALIVE:
        return keepAlive((KeepAliveRequest) request).thenApply(AbstractState::castResponse);
      case SYNC:
        return sync((SyncRequest) request).thenApply(AbstractState::castResponse);
      case APPEND:
        return append((AppendRequest) request).thenApply(AbstractState::castResponse);
      case POLL:
        return poll((PollRequest) request).thenApply(AbstractState::castResponse);
      case VOTE:
        return vote((VoteRequest) request).thenApply(AbstractState::castResponse);
      case SUBMIT:
        return submit((SubmitRequest) request).thenApply(AbstractState::castResponse);
    }
    throw new IllegalArgumentException("invalid request type");
  }

  /**
   * Utility method for casting a response.
   */
  private static <T extends Response> T castResponse(T response) {
    return response;
  }

  /**
   * Handles a join request.
   */
  protected abstract CompletableFuture<JoinResponse> join(JoinRequest request);

  /**
   * Handles a promote request.
   */
  protected abstract CompletableFuture<PromoteResponse> promote(PromoteRequest request);

  /**
   * Handles a demote request.
   */
  protected abstract CompletableFuture<DemoteResponse> demote(DemoteRequest request);

  /**
   * Handles a leave request.
   */
  protected abstract CompletableFuture<LeaveResponse> leave(LeaveRequest request);

  /**
   * Handles a register request.
   */
  protected abstract CompletableFuture<RegisterResponse> register(RegisterRequest request);

  /**
   * Handles a keep alive request.
   */
  protected abstract CompletableFuture<KeepAliveResponse> keepAlive(KeepAliveRequest request);

  /**
   * Handles an append request.
   */
  protected abstract CompletableFuture<AppendResponse> append(AppendRequest request);

  /**
   * Handles a sync request.
   */
  protected abstract CompletableFuture<SyncResponse> sync(SyncRequest request);

  /**
   * Handles a poll request.
   */
  protected abstract CompletableFuture<PollResponse> poll(PollRequest request);

  /**
   * Handles a vote request.
   */
  protected abstract CompletableFuture<VoteResponse> vote(VoteRequest request);

  /**
   * Handles a submit request.
   */
  protected abstract CompletableFuture<SubmitResponse> submit(SubmitRequest request);

  @Override
  public CompletableFuture<Void> close() {
    context.checkThread();
    context.getCluster().member().unregisterHandler(context.getTopic());
    open = false;
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public boolean isClosed() {
    return !open;
  }

  @Override
  public String toString() {
    return String.format("%s[context=%s]", getClass().getSimpleName(), context);
  }

}
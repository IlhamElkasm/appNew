import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { redirectResolverGuard } from './redirect-resolver.guard';

describe('redirectResolverGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => redirectResolverGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});

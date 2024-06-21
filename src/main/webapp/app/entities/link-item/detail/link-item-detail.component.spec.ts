import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { LinkItemDetailComponent } from './link-item-detail.component';

describe('LinkItem Management Detail Component', () => {
  let comp: LinkItemDetailComponent;
  let fixture: ComponentFixture<LinkItemDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LinkItemDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: LinkItemDetailComponent,
              resolve: { linkItem: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LinkItemDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LinkItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load linkItem on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LinkItemDetailComponent);

      // THEN
      expect(instance.linkItem()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});

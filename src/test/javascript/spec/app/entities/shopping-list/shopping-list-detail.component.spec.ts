/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { FyPv6TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ShoppingListDetailComponent } from '../../../../../../main/webapp/app/entities/shopping-list/shopping-list-detail.component';
import { ShoppingListService } from '../../../../../../main/webapp/app/entities/shopping-list/shopping-list.service';
import { ShoppingList } from '../../../../../../main/webapp/app/entities/shopping-list/shopping-list.model';

describe('Component Tests', () => {

    describe('ShoppingList Management Detail Component', () => {
        let comp: ShoppingListDetailComponent;
        let fixture: ComponentFixture<ShoppingListDetailComponent>;
        let service: ShoppingListService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FyPv6TestModule],
                declarations: [ShoppingListDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ShoppingListService,
                    JhiEventManager
                ]
            }).overrideTemplate(ShoppingListDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ShoppingListDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ShoppingListService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ShoppingList(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.shoppingList).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});

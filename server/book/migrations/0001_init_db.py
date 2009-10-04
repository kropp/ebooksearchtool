
from south.db import db
from django.db import models
from server.book.models import *

class Migration:
    
    def forwards(self, orm):
        
        # Adding model 'Alias'
        db.create_table('book_alias', (
            ('id', orm['book.Alias:id']),
            ('name', orm['book.Alias:name']),
        ))
        db.send_create_signal('book', ['Alias'])
        
        # Adding model 'Annotation'
        db.create_table('book_annotation', (
            ('id', orm['book.Annotation:id']),
            ('name', orm['book.Annotation:name']),
        ))
        db.send_create_signal('book', ['Annotation'])
        
        # Adding model 'Series'
        db.create_table('book_series', (
            ('id', orm['book.Series:id']),
            ('name', orm['book.Series:name']),
        ))
        db.send_create_signal('book', ['Series'])
        
        # Adding model 'Book'
        db.create_table('book_book', (
            ('id', orm['book.Book:id']),
            ('title', orm['book.Book:title']),
            ('lang', orm['book.Book:lang']),
        ))
        db.send_create_signal('book', ['Book'])
        
        # Adding model 'BookFile'
        db.create_table('book_bookfile', (
            ('id', orm['book.BookFile:id']),
            ('link', orm['book.BookFile:link']),
            ('size', orm['book.BookFile:size']),
            ('type', orm['book.BookFile:type']),
            ('more_info', orm['book.BookFile:more_info']),
            ('img_link', orm['book.BookFile:img_link']),
        ))
        db.send_create_signal('book', ['BookFile'])
        
        # Adding model 'Tag'
        db.create_table('book_tag', (
            ('id', orm['book.Tag:id']),
            ('name', orm['book.Tag:name']),
        ))
        db.send_create_signal('book', ['Tag'])
        
        # Adding ManyToManyField 'Book.annotation'
        db.create_table('book_book_annotation', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('book', models.ForeignKey(orm.Book, null=False)),
            ('annotation', models.ForeignKey(orm.Annotation, null=False))
        ))
        
    
    
    def backwards(self, orm):
        
        # Deleting model 'Alias'
        db.delete_table('book_alias')
        
        # Deleting model 'Annotation'
        db.delete_table('book_annotation')
        
        # Deleting model 'Series'
        db.delete_table('book_series')
        
        # Deleting model 'Book'
        db.delete_table('book_book')
        
        # Deleting model 'BookFile'
        db.delete_table('book_bookfile')
        
        # Deleting model 'Tag'
        db.delete_table('book_tag')
        
        # Dropping ManyToManyField 'Book.annotation'
        db.delete_table('book_book_annotation')
        
    
    
    models = {
        'book.alias': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'})
        },
        'book.annotation': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.TextField', [], {'unique': 'True'})
        },
        'book.book': {
            'annotation': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Annotation']"}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'lang': ('django.db.models.fields.CharField', [], {'max_length': '2'}),
            'title': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'})
        },
        'book.bookfile': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'img_link': ('django.db.models.fields.URLField', [], {'max_length': '200', 'null': 'True'}),
            'link': ('django.db.models.fields.URLField', [], {'max_length': '200', 'unique': 'True'}),
            'more_info': ('django.db.models.fields.TextField', [], {'null': 'True'}),
            'size': ('django.db.models.fields.IntegerField', [], {}),
            'type': ('django.db.models.fields.CharField', [], {'max_length': '10'})
        },
        'book.series': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'})
        },
        'book.tag': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'})
        }
    }
    
    complete_apps = ['book']
